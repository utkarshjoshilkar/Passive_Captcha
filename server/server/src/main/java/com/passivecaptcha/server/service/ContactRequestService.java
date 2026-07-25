package com.passivecaptcha.server.service;

import com.passivecaptcha.server.model.ApiKey;
import com.passivecaptcha.server.model.ContactRequest;
import com.passivecaptcha.server.model.RequestStatus;
import com.passivecaptcha.server.repository.ApiKeyRepository;
import com.passivecaptcha.server.repository.ContactRequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class ContactRequestService {

    private final ContactRequestRepository contactRequestRepository;
    private final ApiKeyRepository apiKeyRepository;
    private final EmailService emailService;

    public ContactRequestService(ContactRequestRepository contactRequestRepository,
                                 ApiKeyRepository apiKeyRepository,
                                 EmailService emailService) {
        this.contactRequestRepository = contactRequestRepository;
        this.apiKeyRepository = apiKeyRepository;
        this.emailService = emailService;
    }

    @Transactional
    public ContactRequest submitRequest(String fullName, String email, String company, String message) {
        ContactRequest request = new ContactRequest();
        request.setFullName(fullName);
        request.setEmail(email);
        request.setCompany(company);
        request.setMessage(message);
        request.setStatus(RequestStatus.PENDING);

        ContactRequest savedRequest = contactRequestRepository.save(request);
        emailService.sendAdminNotification(savedRequest);
        return savedRequest;
    }

    public Iterable<ContactRequest> getAllRequests() {
        return contactRequestRepository.findAll();
    }

    @Transactional
    public ContactRequest approveRequest(Long requestId) {
        ContactRequest request = contactRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request not found"));

        request.setStatus(RequestStatus.APPROVED);
        contactRequestRepository.save(request);

        ApiKey apiKey = new ApiKey();
        apiKey.setCompanyName(request.getCompany() != null ? request.getCompany() : request.getFullName());
        apiKey.setApiKey(UUID.randomUUID().toString());
        apiKey.setStatus(RequestStatus.APPROVED);
        apiKeyRepository.save(apiKey);

        return request;
    }

    @Transactional
    public ContactRequest rejectRequest(Long requestId) {
        ContactRequest request = contactRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request not found"));

        request.setStatus(RequestStatus.REJECTED);
        return contactRequestRepository.save(request);
    }
}
