package com.passivecaptcha.server.service;

import com.passivecaptcha.server.model.ApiKey;
import com.passivecaptcha.server.model.RequestStatus;
import com.passivecaptcha.server.repository.ApiKeyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ApiKeyService {

    private final ApiKeyRepository apiKeyRepository;

    public ApiKeyService(ApiKeyRepository apiKeyRepository) {
        this.apiKeyRepository = apiKeyRepository;
    }

    public List<ApiKey> getAllApiKeys() {
        return apiKeyRepository.findAll();
    }

    @Transactional
    public ApiKey issueApiKey(String companyName) {
        String key = "pc_live_" + UUID.randomUUID().toString().replace("-", "").substring(0, 16);
        ApiKey apiKey = new ApiKey();
        apiKey.setCompanyName(companyName);
        apiKey.setApiKey(key);
        apiKey.setStatus(RequestStatus.APPROVED);
        ApiKey savedApiKey = apiKeyRepository.save(apiKey);
        return savedApiKey != null ? savedApiKey : apiKey;
    }

    @Transactional
    public ApiKey regenerateApiKey(Long id) {
        ApiKey apiKey = apiKeyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("API key not found"));
        apiKey.setApiKey("pc_live_" + UUID.randomUUID().toString().replace("-", "").substring(0, 16));
        apiKey.setStatus(RequestStatus.APPROVED);
        apiKey.setLastUsed(null);
        apiKey.setUsageCount(0);
        return apiKeyRepository.save(apiKey);
    }

    @Transactional
    public ApiKey revokeApiKey(Long id) {
        ApiKey apiKey = apiKeyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("API key not found"));
        apiKey.setStatus(RequestStatus.REJECTED);
        return apiKeyRepository.save(apiKey);
    }

    @Transactional
    public Optional<ApiKey> validateApiKey(String apiKeyValue) {
        Optional<ApiKey> apiKeyOptional = apiKeyRepository.findByApiKey(apiKeyValue);
        if (apiKeyOptional.isEmpty()) {
            return Optional.empty();
        }

        ApiKey apiKey = apiKeyOptional.get();
        if (apiKey.getStatus() != RequestStatus.APPROVED) {
            return Optional.empty();
        }

        apiKey.setLastUsed(LocalDateTime.now());
        apiKey.setUsageCount(apiKey.getUsageCount() + 1);
        apiKeyRepository.save(apiKey);
        return Optional.of(apiKey);
    }
}
