package com.passivecaptcha.server.controller;

import com.passivecaptcha.server.model.ContactRequest;
import com.passivecaptcha.server.service.ContactRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "http://localhost:3000")
public class ContactRequestController {

    private final ContactRequestService contactRequestService;

    public ContactRequestController(ContactRequestService contactRequestService) {
        this.contactRequestService = contactRequestService;
    }

    @PostMapping("/contact")
    public ResponseEntity<Map<String, Object>> submitContactRequest(@RequestBody Map<String, String> payload) {
        ContactRequest savedRequest = contactRequestService.submitRequest(
                payload.get("fullName"),
                payload.get("email"),
                payload.get("company"),
                payload.get("message")
        );

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Your request has been recorded and an admin notification has been sent.");
        response.put("requestId", savedRequest.getId());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/contact-requests")
    public Iterable<ContactRequest> getContactRequests() {
        return contactRequestService.getAllRequests();
    }

    @PostMapping("/contact-requests/{id}/approve")
    public ResponseEntity<ContactRequest> approveRequest(@PathVariable Long id) {
        return ResponseEntity.ok(contactRequestService.approveRequest(id));
    }

    @PostMapping("/contact-requests/{id}/reject")
    public ResponseEntity<ContactRequest> rejectRequest(@PathVariable Long id) {
        return ResponseEntity.ok(contactRequestService.rejectRequest(id));
    }
}
