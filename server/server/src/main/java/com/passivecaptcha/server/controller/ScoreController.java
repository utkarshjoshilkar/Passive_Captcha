package com.passivecaptcha.server.controller;

import com.passivecaptcha.server.model.BehaviorPayload;
import com.passivecaptcha.server.model.DetectionLabel;
import com.passivecaptcha.server.model.RawEvent;
import com.passivecaptcha.server.model.UserFeatures;
import com.passivecaptcha.server.repository.RawEventRepository;
import com.passivecaptcha.server.repository.UserFeaturesRepository;
import com.passivecaptcha.server.service.ScoreService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST controller for behavioral scoring endpoints.
 *
 * POST /api/v1/score
 *   Accepts a BehaviorPayload (features + raw events), scores it,
 *   persists both the feature vector and the raw event stream,
 *   and returns the scoring result.
 *
 * GET /api/v1/scores   — all stored sessions (admin/testing)
 * GET /api/v1/stats    — aggregate decision summary
 * GET /api/v1/health   — liveness check
 */
@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "http://localhost:3000")
public class ScoreController {

    private final ScoreService            scoreService;
    private final UserFeaturesRepository  featuresRepo;
    private final RawEventRepository      rawEventRepo;

    public ScoreController(ScoreService scoreService,
                           UserFeaturesRepository featuresRepo,
                           RawEventRepository rawEventRepo) {
        this.scoreService  = scoreService;
        this.featuresRepo  = featuresRepo;
        this.rawEventRepo  = rawEventRepo;
    }

    // ─────────────────────────────── POST /score ──────────────────────────────
    @PostMapping("/score")
    public Map<String, Object> calculateScore(@RequestBody BehaviorPayload payload,
                                              HttpServletRequest request) {
        try {
            UserFeatures features = payload.getFeatures();
            if (features == null) {
                throw new IllegalArgumentException("features must not be null");
            }

            // Enrich with server-side metadata
            features.setIpAddress(extractClientIp(request));
            features.setUserAgent(request.getHeader("User-Agent"));

            // Score
            Map<String, Object> evaluation = scoreService.evaluate(features);
            double score    = ((Number) evaluation.get("score")).doubleValue();
            String decision = (String) evaluation.get("decision");

            features.setScore(score);
            features.setDecision(decision);
            features.setLabel(decisionToLabel(decision));

            // Persist feature vector
            UserFeatures saved = featuresRepo.save(features);

            // Persist raw events (link to saved session ID)
            List<RawEvent> rawEvents = payload.getRawEvents();
            if (rawEvents != null && !rawEvents.isEmpty()) {
                rawEvents.forEach(e -> e.setSessionId(saved.getId()));
                rawEventRepo.saveAll(rawEvents);
            }

            // Build response
            Map<String, Object> response = new HashMap<>(evaluation);
            response.put("id",        saved.getId());
            response.put("timestamp", saved.getCreatedAt());
            response.put("status",    "success");
            response.put("message",   friendlyMessage(decision));
            response.put("rawEventCount", rawEvents != null ? rawEvents.size() : 0);
            return response;

        } catch (Exception e) {
            Map<String, Object> err = new HashMap<>();
            err.put("status",  "error");
            err.put("message", "Failed to calculate score: " + e.getMessage());
            return err;
        }
    }

    // ─────────────────────────────── GET /scores ──────────────────────────────
    @GetMapping("/scores")
    public List<UserFeatures> getAllScores() {
        return featuresRepo.findAll();
    }

    // ─────────────────────────────── GET /stats ───────────────────────────────
    @GetMapping("/stats")
    public Map<String, Object> getStats() {
        try {
            List<UserFeatures> all = featuresRepo.findAll();
            long total      = all.size();
            long allowed    = all.stream().filter(f -> "allow".equals(f.getDecision())).count();
            long challenged = all.stream().filter(f -> "challenge".equals(f.getDecision())).count();
            long review     = total - allowed - challenged;

            Map<String, Object> stats = new HashMap<>();
            stats.put("totalRequests",      total);
            stats.put("allowedRequests",    allowed);
            stats.put("challengedRequests", challenged);
            stats.put("reviewRequests",     review);
            stats.put("allowPercentage",    pct(allowed, total));
            stats.put("challengePercentage",pct(challenged, total));
            stats.put("reviewPercentage",   pct(review, total));
            stats.put("status", "success");
            return stats;
        } catch (Exception e) {
            return Map.of("status", "error", "message", e.getMessage());
        }
    }

    // ─────────────────────────────── GET /health ──────────────────────────────
    @GetMapping("/health")
    public Map<String, String> healthCheck() {
        return Map.of("status", "UP", "message", "Passive Captcha Backend is running");
    }

    // ─────────────────────────────── HELPERS ──────────────────────────────────
    private DetectionLabel decisionToLabel(String decision) {
        return switch (decision.toLowerCase()) {
            case "allow"     -> DetectionLabel.HUMAN;
            case "challenge" -> DetectionLabel.BOT;
            default          -> DetectionLabel.UNKNOWN;
        };
    }

    private String friendlyMessage(String decision) {
        return switch (decision) {
            case "allow"     -> "Your behavior appears human-like. Access granted!";
            case "review"    -> "Your behavior is mostly human-like. No action needed.";
            default          -> "Additional verification may be required.";
        };
    }

    private double pct(long n, long total) {
        return total > 0 ? Math.round(n * 100.0 / total * 100.0) / 100.0 : 0;
    }

    private String extractClientIp(HttpServletRequest req) {
        String ip = req.getHeader("X-Forwarded-For");
        if (ip == null || ip.isBlank() || "unknown".equalsIgnoreCase(ip))
            ip = req.getHeader("Proxy-Client-IP");
        if (ip == null || ip.isBlank() || "unknown".equalsIgnoreCase(ip))
            ip = req.getHeader("WL-Proxy-Client-IP");
        if (ip == null || ip.isBlank() || "unknown".equalsIgnoreCase(ip))
            ip = req.getRemoteAddr();
        if (ip != null && ip.contains(","))
            ip = ip.split(",")[0].trim();
        return ip;
    }
}