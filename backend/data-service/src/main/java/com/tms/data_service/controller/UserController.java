package com.tms.data_service.controller;

import com.tms.data_service.dto.UserCreateRequest;
import com.tms.data_service.dto.UserDTO;
import com.tms.data_service.dto.UserUpdateRequest;
import com.tms.data_service.mapper.UserMapper;
import com.tms.data_service.model.Role;
import com.tms.data_service.model.User;
import com.tms.data_service.repository.UserRepository;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@Tag(name = "Users", description = "User management")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping
    @Operation(
        summary = "Create a new user",
        description = "Creates a new user. Does not require authentication."
    )
    public UserDTO createUser(@RequestBody UserCreateRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use"); // Retornando: 401 Unauthorized?
        }
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.valueOf(request.getRole().toUpperCase()));
        
        System.out.println("Passou");

        return UserMapper.toDTO(userRepository.save(user));
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Update a user",
        description = "Updates user data."
    )
    @SecurityRequirement(name = "bearerAuth")
    public UserDTO updateUser(@PathVariable long id, @RequestBody UserUpdateRequest request, Authentication authentication) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        String email = authentication.getName();
        User currentUser = userRepository.findByEmail(email)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated"));

        if (currentUser.getRole() != Role.ADMIN && id != currentUser.getId()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }  

        if (request.getName() != null) {
            user.setName(request.getName());
        }
        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        if (request.getRole() != null) {
            user.setRole(Role.valueOf(request.getRole().toUpperCase()));
        }
        return UserMapper.toDTO(userRepository.save(user));
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Get user by ID",
        description = "Returns user data by ID."
    )
    @SecurityRequirement(name = "bearerAuth")
    public UserDTO getUserById(@PathVariable long id) {
        return userRepository.findById(id)
            .map(UserMapper::toDTO)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Delete user",
        description = "Removes a user by ID."
    )
    @SecurityRequirement(name = "bearerAuth")
    public void deleteUserById(@PathVariable long id, Authentication authentication) {

        if (!userRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        String email = authentication.getName();
        User currentUser = userRepository.findByEmail(email)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated"));

        if (currentUser.getRole() != Role.ADMIN && id != currentUser.getId()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }  

        userRepository.deleteById(id);
    }

    @GetMapping("/me")
    @Operation(
        summary = "Get current user",
        description = "Returns the data of the currently authenticated user."
    )
    @SecurityRequirement(name = "bearerAuth")
    public UserDTO getCurrentUser(Authentication authentication) {
        String email = authentication.getName();
        return userRepository.findByEmail(email)
            .map(UserMapper::toDTO)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }
}