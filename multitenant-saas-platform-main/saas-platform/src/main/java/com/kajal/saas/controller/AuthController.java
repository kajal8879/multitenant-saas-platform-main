package com.kajal.saas.controller;

import com.kajal.saas.dto.AuthDtos.*;
import com.kajal.saas.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @GetMapping("/oauth-success")
    public Map<String, String> oauthSuccess(Authentication authentication) {
        String name = authentication == null ? "unknown" : authentication.getName();
        return Map.of("message", "OAuth login worked. In real frontend exchange this user for JWT.", "user", name);
    }
}
