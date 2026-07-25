package com.passivecaptcha.server.service;

import com.passivecaptcha.server.model.UserFeatures;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Heuristic scoring engine for PassiveCaptcha.
 *
 * Each feature group contributes a normalised sub-score [0, 1].
 * Sub-scores are weighted and summed to produce a final human-likelihood score.
 * Bot-pattern detection can apply a penalty multiplier.
 *
 * This engine is intentionally kept as a transparent, explainable heuristic
 * (suitable for demonstration and research interpretation).
 * The feature vector produced by FeatureExtractorService is designed to be
 * consumed by ML models in future iterations.
 */
@Service
public class ScoreService {

    // ── Feature-group weights (must sum to 1.0) ────────────────────────────────
    private static final double W_MOUSE_MOVES     = 0.08;
    private static final double W_SPEED           = 0.08;
    private static final double W_SPEED_VARIANCE  = 0.07;
    private static final double W_CURVATURE       = 0.07;
    private static final double W_STRAIGHTNESS    = 0.05;  // low = good (human)
    private static final double W_ACCELERATION    = 0.05;
    private static final double W_MOUSE_ENTROPY   = 0.06;
    private static final double W_DIR_CHANGES     = 0.04;
    private static final double W_KEYBOARD        = 0.08;
    private static final double W_SESSION         = 0.10;
    private static final double W_SCROLL          = 0.08;
    private static final double W_SCROLL_ENTROPY  = 0.06;
    private static final double W_CLICK           = 0.05;
    private static final double W_INTERACTION_DEN = 0.05;
    private static final double W_EVENT_RATIO     = 0.08;
    // Total = 1.00

    // ── Bot-pattern penalty ────────────────────────────────────────────────────
    private static final double BOT_PENALTY = 0.3;

    public double calculateScore(UserFeatures f) {
        return (double) evaluate(f).get("score");
    }

    /**
     * Returns a full evaluation map:
     *   score, decision, risk, confidence, featureBreakdown
     */
    public Map<String, Object> evaluate(UserFeatures f) {

        // ── Sub-scores ─────────────────────────────────────────────────────────
        double movesScore      = scorePointerMoves(f.getNumPointerMoves());
        double speedScore      = scoreSpeed(f.getAvgPointerSpeed());
        double varScore        = scoreVariance(f.getSpeedVariance());
        double curvScore       = scoreCurvature(f.getPathCurvature());
        double straightScore   = scoreStraightness(f.getStraightnessRatio());
        double accScore        = scoreAcceleration(f.getAvgPointerAcceleration());
        double mouseEntropy    = scoreEntropy(f.getMouseEntropy());
        double dirScore        = Math.min(f.getMouseDirectionChanges() / 5.0, 1.0);
        double keyScore        = scoreKeyboard(f);
        double sessionScore    = scoreSession(f.getSessionDuration());
        double scrollScore     = scoreScrolls(f.getNumScrolls(), f.getScrollDirectionChanges());
        double scrollEntrScore = scoreEntropy(f.getScrollEntropy());
        double clickScore      = scoreClicks(f);
        double densityScore    = scoreInteractionDensity(f.getInteractionDensity());
        double ratioScore      = scoreEventRatio(f.getEventRatio());

        double raw =
            movesScore     * W_MOUSE_MOVES    +
            speedScore     * W_SPEED          +
            varScore       * W_SPEED_VARIANCE +
            curvScore      * W_CURVATURE      +
            straightScore  * W_STRAIGHTNESS   +
            accScore       * W_ACCELERATION   +
            mouseEntropy   * W_MOUSE_ENTROPY  +
            dirScore       * W_DIR_CHANGES    +
            keyScore       * W_KEYBOARD       +
            sessionScore   * W_SESSION        +
            scrollScore    * W_SCROLL         +
            scrollEntrScore* W_SCROLL_ENTROPY +
            clickScore     * W_CLICK          +
            densityScore   * W_INTERACTION_DEN+
            ratioScore     * W_EVENT_RATIO;

        if (isBotPattern(f)) raw *= BOT_PENALTY;

        double finalScore = clamp(raw);
        String decision   = makeDecision(finalScore);
        String risk       = determineRisk(finalScore);

        Map<String, Object> breakdown = new LinkedHashMap<>();
        breakdown.put("mouseMovement",     round2(movesScore));
        breakdown.put("pointerSpeed",      round2(speedScore));
        breakdown.put("speedVariance",     round2(varScore));
        breakdown.put("pathCurvature",     round2(curvScore));
        breakdown.put("straightness",      round2(straightScore));
        breakdown.put("acceleration",      round2(accScore));
        breakdown.put("mouseEntropy",      round2(mouseEntropy));
        breakdown.put("directionChanges",  round2(dirScore));
        breakdown.put("keyboard",          round2(keyScore));
        breakdown.put("sessionDuration",   round2(sessionScore));
        breakdown.put("scrollBehavior",    round2(scrollScore));
        breakdown.put("scrollEntropy",     round2(scrollEntrScore));
        breakdown.put("clickBehavior",     round2(clickScore));
        breakdown.put("interactionDensity",round2(densityScore));
        breakdown.put("eventRatio",        round2(ratioScore));

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("score",            round2(finalScore));
        result.put("decision",         decision);
        result.put("risk",             risk);
        result.put("confidence",       round2(Math.max(0.5, finalScore)));
        result.put("featureBreakdown", breakdown);
        return result;
    }

    // ── Individual scorers ─────────────────────────────────────────────────────

    private double scorePointerMoves(int moves) {
        if (moves < 5)  return 0.1;
        if (moves < 20) return 0.4;
        return Math.min(moves / 60.0, 1.0);
    }

    private double scoreSpeed(double speed) {
        // Humans: 0.3–2.5 px/ms; bots often 0 or very high
        if (speed <= 0)   return 0.05;
        if (speed < 0.3)  return 0.2;
        if (speed <= 2.5) return 1.0;
        if (speed <= 5.0) return 0.5;
        return 0.1; // unnaturally fast
    }

    private double scoreVariance(double variance) {
        // High variance = human (natural variation)
        if (variance <= 0)    return 0.1;
        if (variance < 0.001) return 0.3;
        if (variance < 0.1)   return 0.7;
        return 1.0;
    }

    private double scoreCurvature(double curvature) {
        if (curvature < 0.1) return 0.1;
        if (curvature < 0.4) return 0.4;
        return Math.min(curvature / 1.5, 1.0);
    }

    /**
     * Straightness ratio close to 1.0 = perfectly straight = bot-like.
     * Humans: 0.3–0.85. Invert the score.
     */
    private double scoreStraightness(double ratio) {
        if (ratio <= 0)   return 0.5; // no data
        if (ratio > 0.95) return 0.1; // suspiciously straight
        if (ratio < 0.3)  return 0.6; // very curved — human
        return 1.0 - ratio; // more curved = better
    }

    private double scoreAcceleration(double acc) {
        // Some acceleration = human; exactly 0 = suspect
        if (acc <= 0)    return 0.1;
        if (acc < 0.001) return 0.4;
        return Math.min(acc / 0.05, 1.0);
    }

    /**
     * Shannon entropy score. Higher entropy = more natural.
     * Max possible entropy for 8 buckets = 3 bits.
     */
    private double scoreEntropy(double entropy) {
        return Math.min(entropy / 3.0, 1.0);
    }

    private double scoreKeyboard(UserFeatures f) {
        if (!f.isUsedKeyboard()) return 0.0;
        double score = 0.5; // base: used keyboard
        if (f.getBackspaceRatio() > 0 && f.getBackspaceRatio() < 0.4) score += 0.3;
        if (f.getAvgKeyIntervalMs() > 50 && f.getAvgKeyIntervalMs() < 1000) score += 0.2;
        return Math.min(score, 1.0);
    }

    private double scoreSession(long durationMs) {
        if (durationMs < 1000)  return 0.05;
        if (durationMs < 3000)  return 0.3;
        if (durationMs < 8000)  return 0.7;
        return Math.min(durationMs / 15000.0, 1.0);
    }

    private double scoreScrolls(int scrolls, int dirChanges) {
        if (scrolls == 0) return 0.05;
        double score = Math.min(scrolls / 12.0, 1.0);
        if (dirChanges > 0) score = Math.min(score + 0.2, 1.0);
        return score;
    }

    private double scoreClicks(UserFeatures f) {
        double score = 0;
        if (f.getClickCount() > 0) {
            score += Math.min(f.getClickCount() / 5.0, 0.5);
            if (f.getClickPositionVariance() > 100) score += 0.3; // varied positions = human
            if (f.getDoubleClickCount() > 0) score += 0.2;
        }
        return Math.min(score, 1.0);
    }

    private double scoreInteractionDensity(double density) {
        // ~2–20 events/second is human-like
        if (density <= 0)   return 0.05;
        if (density < 1)    return 0.2;
        if (density <= 20)  return 1.0;
        return 0.4; // too many events per second = suspicious
    }

    private double scoreEventRatio(double ratio) {
        // mouseMoves / (clicks+scrolls+keys): 5–50 is natural
        if (ratio <= 0)  return 0.1;
        if (ratio < 3)   return 0.3;
        if (ratio <= 60) return 1.0;
        return 0.5;
    }

    // ── Bot pattern detection ──────────────────────────────────────────────────
    private boolean isBotPattern(UserFeatures f) {
        // Zero interaction
        if (f.getNumPointerMoves() < 3 && f.getNumScrolls() == 0 && !f.isUsedKeyboard())
            return true;
        // Extremely fast + minimal movement
        if (f.getSessionDuration() < 500 && f.getNumPointerMoves() < 5)
            return true;
        // Perfectly straight path with many moves
        if (f.getStraightnessRatio() > 0.98 && f.getNumPointerMoves() > 15)
            return true;
        // Zero speed variance with many moves (robotic)
        if (f.getSpeedVariance() < 0.000001 && f.getNumPointerMoves() > 10)
            return true;
        // Many scrolls, no direction changes (programmatic scrolling)
        if (f.getNumScrolls() > 5 && f.getScrollDirectionChanges() == 0
                && f.getScrollEntropy() < 0.3)
            return true;
        return false;
    }

    // ── Decision & Risk ────────────────────────────────────────────────────────
    public String makeDecision(double score) {
        if (score >= 0.60) return "allow";
        if (score >= 0.30) return "review";
        return "challenge";
    }

    private String determineRisk(double score) {
        if (score >= 0.60) return "low";
        if (score >= 0.30) return "medium";
        return "high";
    }

    // ── Utilities ──────────────────────────────────────────────────────────────
    private double clamp(double v)           { return Math.min(Math.max(v, 0.0), 1.0); }
    private double round2(double v)          { return Math.round(v * 100.0) / 100.0; }
}