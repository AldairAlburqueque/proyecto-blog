package com.blog.proyecto_blog.application.usescases.interfaces;

import com.blog.proyecto_blog.application.usescases.dto.request.UpdateProfileRequest;
import com.blog.proyecto_blog.application.usescases.dto.request.UserRequest;
import com.blog.proyecto_blog.application.usescases.dto.response.UserResponse;

import java.util.List;

public interface IUserInterface {
//    UserResponse createUser(UserRequest request);
    UserResponse updateOwnerProfile(UpdateProfileRequest request);
    UserResponse getUserById(Long id);
    List<UserResponse> getAllUsers();
    void deleteUser(Long id);
    UserResponse getCurrentUser();
}
