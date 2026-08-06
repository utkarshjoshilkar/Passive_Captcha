package com.passivecaptcha.server.controller;

import com.passivecaptcha.server.model.ApiKey;
import com.passivecaptcha.server.service.ApiKeyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*")
public class ApiKeyController {

    private final ApiKeyService apiKeyService;

    public ApiKeyController(ApiKeyService apiKeyService) {
        this.apiKeyService = apiKeyService;
    }

    @GetMapping("/api-keys")
    public List<ApiKey> getApiKeys() {
        return apiKeyService.getAllApiKeys();
    }

    @PostMapping("/api-keys")
    public ResponseEntity<ApiKey> issueApiKey(@RequestBody Map<String, String> payload) {
        String companyName = payload.getOrDefault("companyName", "Unknown Company");
        return ResponseEntity.ok(apiKeyService.issueApiKey(companyName));
    }

    @PostMapping("/api-keys/{id}/regenerate")
    public ResponseEntity<ApiKey> regenerateApiKey(@PathVariable Long id) {
        return ResponseEntity.ok(apiKeyService.regenerateApiKey(id));
    }

    @PostMapping("/api-keys/{id}/revoke")
    public ResponseEntity<ApiKey> revokeApiKey(@PathVariable Long id) {
        return ResponseEntity.ok(apiKeyService.revokeApiKey(id));
    }

    @PostMapping("/validate-api-key")
    public ResponseEntity<Map<String, Object>> validateApiKey(@RequestHeader(value = "X-API-Key", required = false) String apiKeyHeader) {
        if (apiKeyHeader == null || apiKeyHeader.isBlank()) {
            Map<String, Object> response = new HashMap<>();
            response.put("valid", false);
            response.put("message", "Missing X-API-Key header");
            return ResponseEntity.status(401).body(response);
        }

        return apiKeyService.validateApiKey(apiKeyHeader)
                .map(apiKey -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("valid", true);
                    response.put("company", apiKey.getCompanyName());
                    response.put("status", apiKey.getStatus().name());
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("valid", false);
                    response.put("message", "Invalid or revoked API key");
                    return ResponseEntity.status(401).body(response);
                });
    }
}
