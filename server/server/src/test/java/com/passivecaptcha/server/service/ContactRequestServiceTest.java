package com.passivecaptcha.server.service;

import com.passivecaptcha.server.model.ContactRequest;
import com.passivecaptcha.server.model.RequestStatus;
import com.passivecaptcha.server.repository.ApiKeyRepository;
import com.passivecaptcha.server.repository.ContactRequestRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ContactRequestServiceTest {

    @Mock
    private ContactRequestRepository contactRequestRepository;

    @Mock
    private ApiKeyRepository apiKeyRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private ContactRequestService contactRequestService;

    @Test
    void submitRequestPersistsRequestAndSendsAdminEmail() {
        ContactRequest savedRequest = new ContactRequest();
        savedRequest.setId(7L);
        savedRequest.setFullName("Jane Doe");
        savedRequest.setEmail("jane@example.com");
        savedRequest.setCompany("Acme Labs");
        savedRequest.setMessage("We would like developer access");
        savedRequest.setStatus(RequestStatus.PENDING);

        when(contactRequestRepository.save(any(ContactRequest.class))).thenReturn(savedRequest);

        ContactRequest result = contactRequestService.submitRequest(
                "Jane Doe",
                "jane@example.com",
                "Acme Labs",
                "We would like developer access"
        );

        assertNotNull(result);
        assertEquals(RequestStatus.PENDING, result.getStatus());
        verify(contactRequestRepository).save(any(ContactRequest.class));
        verify(emailService).sendAdminNotification(any(ContactRequest.class));
    }
}
