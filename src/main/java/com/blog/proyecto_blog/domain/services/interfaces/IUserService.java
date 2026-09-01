package com.blog.proyecto_blog.domain.services.interfaces;

import com.blog.proyecto_blog.application.usescases.dto.request.UpdateProfileRequest;
import com.blog.proyecto_blog.application.usescases.dto.request.UserRequest;
import com.blog.proyecto_blog.application.usescases.dto.response.UserResponse;

import java.util.List;

public interface IUserService {
//    UserResponse createUserServices(UserRequest request);
    UserResponse updateOwnerProfileService(UpdateProfileRequest request);
    UserResponse getUserByIdServices(Long id);
    List<UserResponse> getAllUsersServices();
    void deleteUserServices(Long id);
    UserResponse getCurrentUserService();
}
