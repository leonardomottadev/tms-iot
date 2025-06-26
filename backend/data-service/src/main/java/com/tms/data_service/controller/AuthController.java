package com.tms.data_service.controller;

import org.springframework.web.bind.annotation.RestController;

import com.tms.data_service.dto.TokenResponse;
import com.tms.data_service.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@Tag(name = "Authentication", description = "JWT token generation")
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthService authenticationService;
    
    @PostMapping("/auth")
    @Operation(
        summary = "Authenticate and obtain JWT token",
        description = "Generates a JWT token for the authenticated user using Basic Auth."
    )
    @SecurityRequirement(name = "basicAuth")
    public TokenResponse authenticate(Authentication authentication) {
        if (authentication != null) {
            System.out.println("Authentication Name (Principal): " + authentication.getName());
        }
        String token = authenticationService.authenticate(authentication);
        return new TokenResponse(token);
    }
}
