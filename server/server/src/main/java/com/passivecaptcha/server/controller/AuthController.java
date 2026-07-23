package com.passivecaptcha.server.controller;

import com.passivecaptcha.server.model.AppUser;
import com.passivecaptcha.server.repository.AppUserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    private final AppUserRepository appUserRepository;

    public AuthController(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AppUser user) {
        if (appUserRepository.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username already exists");
        }
 
        // In a real app, hash the password!
        appUserRepository.save(user);

        return ResponseEntity.ok(Map.of("message", "User registered successfully"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AppUser loginRequest) {
        Optional<AppUser> userOpt = appUserRepository.findByUsername(loginRequest.getUsername());

        if (userOpt.isPresent() && userOpt.get().getPassword().equals(loginRequest.getPassword())) {
            // In a real app, return JWT
            Map<String, Object> response = new HashMap<>();
            response.put("token", "mock-jwt-token-" + userOpt.get().getId());
            response.put("userId", userOpt.get().getId());
            response.put("username", userOpt.get().getUsername());
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }
}
