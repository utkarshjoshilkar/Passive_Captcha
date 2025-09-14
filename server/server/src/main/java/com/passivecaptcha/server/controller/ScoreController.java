package com.passivecaptcha.server.controller;

import com.passivecaptcha.server.model.UserFeatures;
import com.passivecaptcha.server.repository.UserFeaturesRepository;
import com.passivecaptcha.server.service.ScoreService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*") // Allow frontend requests
public class ScoreController {

    private final ScoreService scoreService;
    private final UserFeaturesRepository userFeaturesRepository;

    public ScoreController(ScoreService scoreService, UserFeaturesRepository userFeaturesRepository) {
        this.scoreService = scoreService;
        this.userFeaturesRepository = userFeaturesRepository;
    }

    @PostMapping("/score")
    public Map<String, Object> calculateScore(@RequestBody UserFeatures features, HttpServletRequest request) {
        // Capture additional data from the request
        features.setIpAddress(getClientIpAddress(request));
        features.setUserAgent(request.getHeader("User-Agent"));

        // Save to database
        UserFeatures savedFeatures = userFeaturesRepository.save(features);

        // Calculate score
        double score = scoreService.calculateScore(savedFeatures);
        String decision = score > 0.5 ? "allow" : "challenge";

        // Prepare response
        Map<String, Object> response = new HashMap<>();
        response.put("score", Math.round(score * 100.0) / 100.0); // Round to 2 decimal places
        response.put("decision", decision);
        response.put("id", savedFeatures.getId());
        response.put("timestamp", savedFeatures.getCreatedAt());

        return response;
    }

    // Get all records (for admin/testing)
    @GetMapping("/scores")
    public List<UserFeatures> getAllScores() {
        return userFeaturesRepository.findAll();
    }

    // Get stats summary
    @GetMapping("/stats")
    public Map<String, Object> getStats() {
        List<UserFeatures> allScores = userFeaturesRepository.findAll();

        long totalRequests = allScores.size();
        long allowedRequests = allScores.stream()
                .filter(features -> scoreService.calculateScore(features) > 0.5)
                .count();
        long challengedRequests = totalRequests - allowedRequests;

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalRequests", totalRequests);
        stats.put("allowedRequests", allowedRequests);
        stats.put("challengedRequests", challengedRequests);
        stats.put("allowPercentage", totalRequests > 0 ?
                Math.round((allowedRequests * 100.0 / totalRequests) * 100.0) / 100.0 : 0);

        return stats;
    }

    // Get client IP address (handles proxies)
    private String getClientIpAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }

        // If multiple IPs, take the first one
        if (ipAddress != null && ipAddress.contains(",")) {
            ipAddress = ipAddress.split(",")[0].trim();
        }

        return ipAddress;
    }

    // DTO for request (optional - you can keep using UserFeatures directly)
    public static class ScoreRequest {
        private int numPointerMoves;
        private double avgPointerSpeed;
        private boolean usedKeyboard;
        private long sessionDuration;

        // Getters and setters
        public int getNumPointerMoves() { return numPointerMoves; }
        public void setNumPointerMoves(int numPointerMoves) { this.numPointerMoves = numPointerMoves; }

        public double getAvgPointerSpeed() { return avgPointerSpeed; }
        public void setAvgPointerSpeed(double avgPointerSpeed) { this.avgPointerSpeed = avgPointerSpeed; }

        public boolean isUsedKeyboard() { return usedKeyboard; }
        public void setUsedKeyboard(boolean usedKeyboard) { this.usedKeyboard = usedKeyboard; }

        public long getSessionDuration() { return sessionDuration; }
        public void setSessionDuration(long sessionDuration) { this.sessionDuration = sessionDuration; }
    }
}

