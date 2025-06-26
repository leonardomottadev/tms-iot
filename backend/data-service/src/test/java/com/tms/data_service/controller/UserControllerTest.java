package com.tms.data_service.controller;

import com.tms.data_service.dto.UserCreateRequest;
import com.tms.data_service.dto.UserDTO;
import com.tms.data_service.dto.UserUpdateRequest;
import com.tms.data_service.model.Role;
import com.tms.data_service.model.User;
import com.tms.data_service.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private UserController userController;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        userController = new UserController(userRepository, passwordEncoder);
    }

    @Test
    void shouldCreateUserWhenEmailIsAvailable() {
        UserCreateRequest request = new UserCreateRequest("Test User", "test@email.com", "password123", "ADMIN");

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");

        User savedUser = new User(1L, request.getName(), request.getEmail(), "encodedPassword", Role.ADMIN);
        when(userRepository.save(ArgumentMatchers.any(User.class))).thenReturn(savedUser);

        UserDTO dto = userController.createUser(request);

        assertEquals("Test User", dto.getName());
        assertEquals("test@email.com", dto.getEmail());
        assertEquals("ADMIN", dto.getRole());
    }

    @Test
    void shouldThrowConflictWhenEmailAlreadyExists() {
        UserCreateRequest request = new UserCreateRequest("Test User", "existing@email.com", "password", "USER");

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(new User()));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> userController.createUser(request));

        assertEquals(409, ex.getStatusCode().value());
    }

    @Test
    void shouldReturnUserById() {
        User user = new User(1L, "John Doe", "john@email.com", "password", Role.USER);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserDTO dto = userController.getUserById(1L);

        assertEquals("John Doe", dto.getName());
    }

    @Test
    void shouldThrowNotFoundWhenUserDoesNotExist() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> userController.getUserById(99L));
    }

    @Test
    void shouldReturnCurrentUser() {
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("user@email.com");

        User user = new User(1L, "Current User", "user@email.com", "password", Role.USER);

        when(userRepository.findByEmail("user@email.com")).thenReturn(Optional.of(user));

        UserDTO dto = userController.getCurrentUser(auth);

        assertEquals("Current User", dto.getName());
    }

    @Test
    void shouldUpdateUserWhenAuthorized() {
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("admin@email.com");

        User existingUser = new User(2L, "Old Name", "old@email.com", "oldpass", Role.USER);
        User adminUser = new User(1L, "Admin", "admin@email.com", "adminpass", Role.ADMIN);

        UserUpdateRequest request = new UserUpdateRequest("New Name", "new@email.com", "newpass", "USER");

        when(userRepository.findById(2L)).thenReturn(Optional.of(existingUser));
        when(userRepository.findByEmail("admin@email.com")).thenReturn(Optional.of(adminUser));
        when(passwordEncoder.encode("newpass")).thenReturn("encodedNewPass");
        when(userRepository.save(ArgumentMatchers.any(User.class))).thenReturn(existingUser);

        UserDTO dto = userController.updateUser(2L, request, auth);

        assertEquals("New Name", dto.getName());
        assertEquals("new@email.com", dto.getEmail());
    }

    @Test
    void shouldDenyUpdateWhenNotAdminAndDifferentUser() {
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("other@email.com");

        User existingUser = new User(2L, "User", "user@email.com", "pass", Role.USER);
        User loggedUser = new User(3L, "Other", "other@email.com", "pass", Role.USER);

        UserUpdateRequest request = new UserUpdateRequest("New", null, null, null);

        when(userRepository.findById(2L)).thenReturn(Optional.of(existingUser));
        when(userRepository.findByEmail("other@email.com")).thenReturn(Optional.of(loggedUser));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> userController.updateUser(2L, request, auth));

        assertEquals(403, ex.getStatusCode().value());
    }

    @Test
    void shouldDeleteUserWhenAuthorized() {
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("admin@email.com");

        User adminUser = new User(1L, "Admin", "admin@email.com", "pass", Role.ADMIN);

        when(userRepository.existsById(2L)).thenReturn(true);
        when(userRepository.findByEmail("admin@email.com")).thenReturn(Optional.of(adminUser));

        userController.deleteUserById(2L, auth);

        verify(userRepository, times(1)).deleteById(2L);
    }

    @Test
    void shouldDenyDeleteWhenNotAdminAndDifferentUser() {
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("other@email.com");

        User loggedUser = new User(3L, "Other", "other@email.com", "pass", Role.USER);

        when(userRepository.existsById(2L)).thenReturn(true);
        when(userRepository.findByEmail("other@email.com")).thenReturn(Optional.of(loggedUser));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> userController.deleteUserById(2L, auth));

        assertEquals(403, ex.getStatusCode().value());
    }

    @Test
    void shouldThrowNotFoundWhenDeletingNonExistingUser() {
        Authentication auth = mock(Authentication.class);

        when(userRepository.existsById(2L)).thenReturn(false);

        assertThrows(ResponseStatusException.class, () -> userController.deleteUserById(2L, auth));
    }
}
