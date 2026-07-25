package com.passivecaptcha.server.service;

import com.passivecaptcha.server.model.Admin;
import com.passivecaptcha.server.repository.AdminRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AdminService(AdminRepository adminRepository,
                        PasswordEncoder passwordEncoder,
                        JwtService jwtService) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public String authenticate(String username, String password) {

        Optional<Admin> adminOptional = adminRepository.findByUsername(username);

        if (adminOptional.isEmpty()) {
            throw new IllegalArgumentException("Invalid admin credentials");
        }

        Admin admin = adminOptional.get();

        if (!passwordEncoder.matches(password, admin.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid admin credentials");
        }

        // Generate a real JWT instead of a dummy string
        return jwtService.generateToken(
                admin.getUsername(),
                admin.getRole()
        );
    }
}