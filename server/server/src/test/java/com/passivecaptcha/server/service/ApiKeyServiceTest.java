package com.passivecaptcha.server.service;

import com.passivecaptcha.server.model.ApiKey;
import com.passivecaptcha.server.model.RequestStatus;
import com.passivecaptcha.server.repository.ApiKeyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApiKeyServiceTest {

    @Mock
    private ApiKeyRepository apiKeyRepository;

    @InjectMocks
    private ApiKeyService apiKeyService;

    @Test
    void issueApiKeyCreatesSecureKeyForCompany() {
        ApiKey issued = apiKeyService.issueApiKey("Acme Labs");

        assertNotNull(issued);
        assertEquals("Acme Labs", issued.getCompanyName());
        assertEquals(RequestStatus.APPROVED, issued.getStatus());
        assertNotNull(issued.getApiKey());
        verify(apiKeyRepository).save(any(ApiKey.class));
    }

    @Test
    void validateApiKeyReturnsKeyAndIncrementsUsage() {
        ApiKey apiKey = new ApiKey();
        apiKey.setId(2L);
        apiKey.setApiKey("pc_live_testkey");
        apiKey.setCompanyName("Acme Labs");
        apiKey.setStatus(RequestStatus.APPROVED);
        apiKey.setUsageCount(0);

        when(apiKeyRepository.findByApiKey("pc_live_testkey")).thenReturn(Optional.of(apiKey));

        Optional<ApiKey> result = apiKeyService.validateApiKey("pc_live_testkey");

        assertEquals(Optional.of(apiKey), result);
        assertEquals(1L, apiKey.getUsageCount());
        verify(apiKeyRepository).save(apiKey);
    }
}
