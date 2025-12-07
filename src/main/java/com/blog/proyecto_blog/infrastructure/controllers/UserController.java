package com.blog.proyecto_blog.infrastructure.controllers;

import com.blog.proyecto_blog.application.usescases.dto.request.UserRequest;
import com.blog.proyecto_blog.application.usescases.dto.response.UserResponse;
import com.blog.proyecto_blog.application.usescases.interfaces.IUserInterface;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final IUserInterface userInterface;

    @PostMapping("/save")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest userRequest) {
        return ResponseEntity.ok(userInterface.createUser(userRequest));
    }

    @PutMapping("update/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable String id,
            @RequestBody UserRequest request
    ) {
        Long idUser = Long.parseLong(id);
        return ResponseEntity.ok(userInterface.updateUser(idUser, request));
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
}
