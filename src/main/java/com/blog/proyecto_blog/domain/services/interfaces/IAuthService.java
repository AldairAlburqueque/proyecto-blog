package com.blog.proyecto_blog.domain.services.interfaces;

import com.blog.proyecto_blog.application.usescases.dto.request.LoginRequest;
import com.blog.proyecto_blog.application.usescases.dto.request.UserRequest;
import com.blog.proyecto_blog.application.usescases.dto.response.LoginResponse;
import com.blog.proyecto_blog.application.usescases.dto.response.UserResponse;
import org.springframework.http.ResponseEntity;

public interface IAuthService {
    LoginResponse loginService(LoginRequest request);
    UserResponse createUserServices(UserRequest request);
}

