package com.blog.proyecto_blog.domain.services.interfaces;

import com.blog.proyecto_blog.application.usescases.dto.request.LoginRequest;
import com.blog.proyecto_blog.application.usescases.dto.request.RegisterUserRequest;
import com.blog.proyecto_blog.application.usescases.dto.response.LoginResponse;
import com.blog.proyecto_blog.application.usescases.dto.response.UserResponse;

public interface IAuthService {
    LoginResponse loginService(LoginRequest request);
    UserResponse createUserServices(RegisterUserRequest request);
}

