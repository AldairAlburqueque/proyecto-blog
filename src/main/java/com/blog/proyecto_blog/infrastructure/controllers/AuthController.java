package com.blog.proyecto_blog.infrastructure.controllers;


import com.blog.proyecto_blog.application.usescases.dto.request.LoginRequest;
import com.blog.proyecto_blog.application.usescases.dto.request.UserRequest;
import com.blog.proyecto_blog.application.usescases.dto.response.LoginResponse;
import com.blog.proyecto_blog.application.usescases.dto.response.UserResponse;
import com.blog.proyecto_blog.application.usescases.interfaces.IAuthInterface;
import com.blog.proyecto_blog.application.usescases.interfaces.IUserInterface;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final IAuthInterface loginInterface;

    @PostMapping("/save")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest userRequest) {
        UserResponse response = loginInterface.createUser(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
//        UserEntity user = userRepository.findByEmail(request.getEmail())
//                .orElseThrow(() -> new RuntimeException("Email o contraseña incorrecta"));
//
//        // Verificar contraseña si es correcta
//        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
//            throw new RuntimeException("Email o contraseña incorrecta");
//        }
//
//        String token = jwtConfig.getJWTToken(user);
//
//        LoginResponse response = new LoginResponse(
//                token,
//                user.getEmail(),
//                user.getName(),
//                user.getRol().getRol()
//        );
//
//        return ResponseEntity.ok(response);
        LoginResponse response = loginInterface.login(request);
        return  ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }



}
