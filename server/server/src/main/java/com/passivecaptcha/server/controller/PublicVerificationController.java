package com.passivecaptcha.server.controller;

import com.passivecaptcha.server.model.Project;
import com.passivecaptcha.server.service.ProjectService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/public")
@CrossOrigin(origins = "*") // We handle specific origin checks manually in the controller
public class PublicVerificationController {

    private final ProjectService projectService;
    private final RestTemplate restTemplate = new RestTemplate();

    public PublicVerificationController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestBody Map<String, Object> payload,
            @RequestHeader(value = "X-API-KEY", required = false) String apiKey,
            HttpServletRequest request) {

        // 1. Validate API Key
        if (apiKey == null || apiKey.isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Missing API Key");
        }

        Project project = projectService.getProjectByApiKey(apiKey);
        if (project == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid API Key");
        }

        // 2. Validate Origin (CORS enforcement)
        String origin = request.getHeader("Origin");
        // Remove protocol (http://, https://) for comparison if needed, or check mostly
        // string match
        if (origin != null && !origin.contains(project.getDomain())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Origin mismatch. Project domain: " + project.getDomain());
        }

        // 3. Call ML Service
        String mlServiceUrl = "http://localhost:8000/predict"; // TODO: Use config/env variable
        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(mlServiceUrl, payload, Map.class);
            Map<String, Object> mlBody = response.getBody();

            if (mlBody == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Empty response from ML service");
            }

            double score = 0.0;
            if (mlBody.containsKey("score")) {
                Object scoreObj = mlBody.get("score");
                if (scoreObj instanceof Number) {
                    score = ((Number) scoreObj).doubleValue();
                }
            }

            // 4. Determine Decision (Pass/Fail)
            // Strict < 0.3 = Bot, >= 0.6 = Human, else Review
            String status = "review";
            if (score >= 0.6) {
                status = "human";
            } else if (score < 0.3) {
                status = "bot";
            }

            Map<String, Object> finalResponse = new HashMap<>();
            finalResponse.put("status", status); // "human", "bot", "review"
            finalResponse.put("score", score);
            finalResponse.put("authorized", "human".equals(status));
            finalResponse.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.ok(finalResponse);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error connecting to ML Service: " + e.getMessage());
        }
    }
}
