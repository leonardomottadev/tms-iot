package com.tms.data_service.controller;

import org.springframework.web.bind.annotation.RestController;

import com.tms.data_service.dto.TokenResponse;
import com.tms.data_service.service.AuthService;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
public class AuthController {
    
    private AuthService authenticationService;

    public AuthController(AuthService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/auth")
    public TokenResponse authenticate(Authentication authentication) {
        if (authentication != null) {
            System.out.println("Authentication Name (Principal): " + authentication.getName());
        }
        String token = authenticationService.authenticate(authentication);
        return new TokenResponse(token);
    }
}
