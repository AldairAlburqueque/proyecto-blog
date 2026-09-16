package com.blog.proyecto_blog.infrastructure.controllers;

import com.blog.proyecto_blog.application.usescases.dto.request.ChangePasswordRequest;
import com.blog.proyecto_blog.application.usescases.dto.request.UpdateProfileRequest;
import com.blog.proyecto_blog.application.usescases.dto.request.UserRequest;
import com.blog.proyecto_blog.application.usescases.dto.response.UserResponse;
import com.blog.proyecto_blog.application.usescases.interfaces.IUserInterface;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final IUserInterface userInterface;
    private final PasswordEncoder passwordEncoder;

    @PutMapping("/me")
    public ResponseEntity<UserResponse> updateUser(
            @Valid @RequestBody UpdateProfileRequest request
    ) {
        return ResponseEntity.ok(userInterface.updateOwnerProfile(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable String id) {
        Long idUser = Long.parseLong(id);
        return ResponseEntity.ok(userInterface.getUserById(idUser));
    }

    @GetMapping("/list")
    public ResponseEntity<List<UserResponse>> getAllUsers() {

        return ResponseEntity.ok(userInterface.getAllUsers());
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userInterface.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser() {
        return ResponseEntity.ok(userInterface.getCurrentUser());
    }

    @PutMapping("/me/password")
    public ResponseEntity<Void> changeOwnPassword(
            @Valid @RequestBody ChangePasswordRequest request
            ) {
        userInterface.chageOwnPasswordService(request);

        return ResponseEntity.noContent().build();
    }
}
