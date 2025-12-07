package com.blog.proyecto_blog.application.usescases.implementation;

import com.blog.proyecto_blog.application.usescases.dto.request.UserRequest;
import com.blog.proyecto_blog.application.usescases.dto.response.UserResponse;
import com.blog.proyecto_blog.application.usescases.interfaces.IUserInterface;
import com.blog.proyecto_blog.application.usescases.mappers.UserMapper;
import com.blog.proyecto_blog.domain.services.interfaces.IUserService;
import com.blog.proyecto_blog.infrastructure.database.repositories.RolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserImplementation implements IUserInterface {
    private final IUserService iUserService;

    @Override
    public UserResponse createUser(UserRequest userRequest) {
        return iUserService.createUserServices(userRequest);
    }

    @Override
    public UserResponse updateUser(Long id, UserRequest userRequest) {
        return iUserService.updateUserServices(id, userRequest);
    }

    @Override
    public UserResponse getUserById(Long id) {
        return iUserService.getUserByIdServices(id);
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return iUserService.getAllUsersServices();
    }

    @Override
    public void deleteUser(Long id) {
        iUserService.deleteUserServices(id);
    }
}
