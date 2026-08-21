package com.luckspinquest.controller;

import com.luckspinquest.entity.User;
import com.luckspinquest.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody RegisterRequest request
    ) {
        try {
            User user = authService.register(
                    request.username(),
                    request.name(),
                    request.email(),
                    request.phone(),
                    request.password()
            );

            Map<String, Object> response = new LinkedHashMap<>();
            response.put("message", "Registration successful");
            response.put("userId", user.getUserId());
            response.put("username", user.getUserUsername());
            response.put("email", user.getUserEmail());

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequest request
    ) {
        try {
            return ResponseEntity.ok(
                    authService.login(
                            request.usernameOrEmail(),
                            request.password()
                    )
            );

        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh() {
        return ResponseEntity.ok(
                Map.of("message", "Refresh token endpoint")
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok(
                Map.of("message", "Logout endpoint")
        );
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword() {
        return ResponseEntity.ok(
                Map.of("message", "Forgot password endpoint")
        );
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword() {
        return ResponseEntity.ok(
                Map.of("message", "Reset password endpoint")
        );
    }

    @PostMapping("/verify-email")
    public ResponseEntity<?> verifyEmail() {
        return ResponseEntity.ok(
                Map.of("message", "Verify email endpoint")
        );
    }

    @PostMapping("/resend-verification")
    public ResponseEntity<?> resendVerification() {
        return ResponseEntity.ok(
                Map.of("message", "Resend verification endpoint")
        );
    }

    public record RegisterRequest(
            String username,
            String name,
            String email,
            String phone,
            String password
    ) {}

    public record LoginRequest(
            String usernameOrEmail,
            String password
    ) {}
}
