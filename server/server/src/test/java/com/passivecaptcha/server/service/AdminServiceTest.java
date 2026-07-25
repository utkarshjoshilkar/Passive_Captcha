package com.passivecaptcha.server.service;

import com.passivecaptcha.server.model.Admin;
import com.passivecaptcha.server.repository.AdminRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AdminService adminService;

    @Test
    void authenticateReturnsTokenForValidAdmin() {
        Admin admin = new Admin();
        admin.setUsername("admin");
        admin.setPasswordHash("encoded");

        when(adminRepository.findByUsername("admin")).thenReturn(Optional.of(admin));
        when(passwordEncoder.matches("admin123", "encoded")).thenReturn(true);

        String token = adminService.authenticate("admin", "admin123");

        assertNotNull(token);
    }

    @Test
    void authenticateThrowsForInvalidAdminPassword() {
        Admin admin = new Admin();
        admin.setUsername("admin");
        admin.setPasswordHash("encoded");

        when(adminRepository.findByUsername("admin")).thenReturn(Optional.of(admin));
        when(passwordEncoder.matches("wrong", "encoded")).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> adminService.authenticate("admin", "wrong"));
    }
}
