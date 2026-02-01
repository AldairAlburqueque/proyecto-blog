package com.blog.proyecto_blog.application.usescases.implementation;

import com.blog.proyecto_blog.application.usescases.dto.request.LoginRequest;
import com.blog.proyecto_blog.application.usescases.dto.request.UserRequest;
import com.blog.proyecto_blog.application.usescases.dto.response.LoginResponse;
import com.blog.proyecto_blog.application.usescases.dto.response.UserResponse;
import com.blog.proyecto_blog.application.usescases.interfaces.IAuthInterface;
import com.blog.proyecto_blog.domain.services.interfaces.IAuthService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthImplementacion implements IAuthInterface {
   private final IAuthService iAuthService;

    @Override
    public LoginResponse login(LoginRequest request) {
        return iAuthService.loginService(request);
    }

    @Override
    public UserResponse createUser(UserRequest request) {
        return iAuthService.createUserServices(request);
    }
}
