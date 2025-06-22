package com.tms.data_service.controller;

import org.springframework.web.bind.annotation.RestController;

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
    public String authenticate(Authentication authentication) {

        System.out.println("Requisição recebida em /auth");
        try {
            if (authentication != null) {
                System.out.println("Authentication object: " + authentication);
                System.out.println("Authentication Name (Principal): " + authentication.getName());
                System.out.println("Authentication isAuthenticated(): " + authentication.isAuthenticated());
                authentication.getAuthorities().forEach(a -> System.out.println("Authority: " + a.getAuthority()));
            } else {
                System.out.println("Authentication object is null.");
            }
        } catch(Exception exception){
            System.out.println(exception.getMessage());
        }


        return authenticationService.authenticate(authentication);
    }
}
