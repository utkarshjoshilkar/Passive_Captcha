package com.passivecaptcha.server.service;

import com.passivecaptcha.server.model.ContactRequest;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    public void sendAdminNotification(ContactRequest request) {
        System.out.println("Email notification queued for admin: " + request.getEmail());
    }
}
