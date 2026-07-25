package com.passivecaptcha.server.controller;

import com.passivecaptcha.server.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin")
@CrossOrigin(origins = "http://localhost:3000")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> payload) {
        try {
            String token = adminService.authenticate(payload.get("username"), payload.get("password"));
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("status", "success");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException ex) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", ex.getMessage());
            return ResponseEntity.status(401).body(response);
        }
    }

    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> me() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Admin session active");
        return ResponseEntity.ok(response);
    }
}
