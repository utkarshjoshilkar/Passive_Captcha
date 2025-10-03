package com.passivecaptcha.server.service;

import com.passivecaptcha.server.model.UserFeatures;
import org.springframework.stereotype.Service;

@Service
public class ScoreService {

    // More strict weights - focus on detecting bots
    private static final double POINTER_MOVE_WEIGHT = 0.15;
    private static final double SPEED_WEIGHT = 0.15;
    private static final double KEYBOARD_WEIGHT = 0.10;
    private static final double SESSION_WEIGHT = 0.15;
    private static final double SCROLL_WEIGHT = 0.20;
    private static final double CURVATURE_WEIGHT = 0.15;
    private static final double SCROLL_DIRECTION_WEIGHT = 0.10;

    public double calculateScore(UserFeatures features) {
        double score = 0.0;

        System.out.println("🔍 Analyzing behavior features:");
        System.out.println("  - Mouse moves: " + features.getNumPointerMoves());
        System.out.println("  - Avg speed: " + features.getAvgPointerSpeed());
        System.out.println("  - Path curvature: " + features.getPathCurvature());
        System.out.println("  - Keyboard used: " + features.isUsedKeyboard());
        System.out.println("  - Session duration: " + features.getSessionDuration());
        System.out.println("  - Scrolls: " + features.getNumScrolls());
        System.out.println("  - Scroll direction changes: " + features.getScrollDirectionChanges());

        // 1. Pointer moves: Very few moves = bot-like
        double moveScore = 0.0;
        if (features.getNumPointerMoves() < 5) {
            moveScore = 0.1; // Very few moves = suspicious
        } else if (features.getNumPointerMoves() < 15) {
            moveScore = 0.3;
        } else {
            moveScore = Math.min(features.getNumPointerMoves() / 50.0, 1.0);
        }
        score += moveScore * POINTER_MOVE_WEIGHT;
        System.out.println("  - Move score: " + moveScore);

        // 2. Speed: Too perfect or too fast = bot-like
        double speed = features.getAvgPointerSpeed();
        double speedScore = 0.0;
        if (speed < 0.1) {
            speedScore = 0.1; // Too slow/unmoving
        } else if (speed > 3.0) {
            speedScore = 0.2; // Too fast/robotic
        } else if (speed >= 0.5 && speed <= 2.0) {
            speedScore = 1.0; // Human-like range
        } else {
            speedScore = 0.5; // Borderline
        }
        score += speedScore * SPEED_WEIGHT;
        System.out.println("  - Speed score: " + speedScore);

        // 3. Path curvature: Very straight lines = bot-like
        double curvature = features.getPathCurvature();
        double curvatureScore = 0.0;
        if (curvature < 0.3) {
            curvatureScore = 0.1; // Very straight = bot
        } else if (curvature < 0.8) {
            curvatureScore = 0.4; // Some curvature
        } else {
            curvatureScore = Math.min(curvature / 2.0, 1.0); // Curvy = human
        }
        score += curvatureScore * CURVATURE_WEIGHT;
        System.out.println("  - Curvature score: " + curvatureScore);

        // 4. Keyboard usage: No keyboard = suspicious
        if (features.isUsedKeyboard()) {
            score += KEYBOARD_WEIGHT;
            System.out.println("  - Keyboard bonus: +" + KEYBOARD_WEIGHT);
        } else {
            System.out.println("  - No keyboard usage");
        }

        // 5. Session duration: Very short = bot-like
        double sessionScore = 0.0;
        if (features.getSessionDuration() < 2000) {
            sessionScore = 0.1; // Very short = bot
        } else if (features.getSessionDuration() < 5000) {
            sessionScore = 0.5; // Short session
        } else {
            sessionScore = Math.min(features.getSessionDuration() / 10000.0, 1.0);
        }
        score += sessionScore * SESSION_WEIGHT;
        System.out.println("  - Session score: " + sessionScore);

        // 6. Scroll behavior: No scrolling or minimal = bot-like
        double scrollScore = 0.0;
        if (features.getNumScrolls() == 0) {
            scrollScore = 0.1; // No scrolling = very suspicious
        } else if (features.getNumScrolls() < 3) {
            scrollScore = 0.3; // Minimal scrolling
        } else {
            scrollScore = Math.min(features.getNumScrolls() / 10.0, 1.0);

            // Bonus for direction changes
            if (features.getScrollDirectionChanges() > 0) {
                scrollScore += 0.3;
            }
        }
        scrollScore = Math.min(scrollScore, 1.0);
        score += scrollScore * SCROLL_WEIGHT;
        System.out.println("  - Scroll score: " + scrollScore);

        // 7. Scroll direction: No changes = bot-like
        double directionScore = Math.min(features.getScrollDirectionChanges() / 3.0, 1.0);
        score += directionScore * SCROLL_DIRECTION_WEIGHT;
        System.out.println("  - Direction score: " + directionScore);

        // Penalize obvious bot patterns
        if (isBotPattern(features)) {
            score *= 0.3; // Heavy penalty for bot patterns
            System.out.println("  ⚠️ Bot pattern detected - heavy penalty applied");
        }

        double finalScore = Math.min(Math.max(score, 0.0), 1.0);
        System.out.println("🎯 Final score: " + finalScore);

        return finalScore;
    }

    private boolean isBotPattern(UserFeatures features) {
        // Pattern 1: No mouse movement + no scrolling + no keyboard
        if (features.getNumPointerMoves() < 3 &&
                features.getNumScrolls() == 0 &&
                !features.isUsedKeyboard()) {
            return true;
        }

        // Pattern 2: Very short session with immediate action
        if (features.getSessionDuration() < 1000 &&
                features.getNumPointerMoves() < 5) {
            return true;
        }

        // Pattern 3: Perfectly straight lines (very low curvature)
        if (features.getPathCurvature() < 0.2 &&
                features.getNumPointerMoves() > 10) {
            return true;
        }

        // Pattern 4: No scrolling direction changes (only one direction)
        if (features.getNumScrolls() > 2 &&
                features.getScrollDirectionChanges() == 0) {
            return true;
        }

        return false;
    }

    public String makeDecision(double score) {
        // More strict thresholds
        if (score >= 0.6) {
            return "allow"; // Definitely human
        } else if (score >= 0.3) {
            return "review"; // Suspicious
        } else {
            return "challenge"; // Likely bot
        }
    }
}