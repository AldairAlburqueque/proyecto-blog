package com.blog.proyecto_blog.application.usescases.implementation;

import com.blog.proyecto_blog.application.usescases.dto.request.ChangePasswordRequest;
import com.blog.proyecto_blog.application.usescases.dto.request.UpdateProfileRequest;
import com.blog.proyecto_blog.application.usescases.dto.request.UserRequest;
import com.blog.proyecto_blog.application.usescases.dto.response.UserResponse;
import com.blog.proyecto_blog.application.usescases.interfaces.IUserInterface;
import com.blog.proyecto_blog.domain.services.interfaces.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserImplementation implements IUserInterface {
    private final IUserService iUserService;

    @Override
    public UserResponse updateOwnerProfile(UpdateProfileRequest request) {
        return iUserService.updateOwnerProfileService(request);
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

    @Override
    public UserResponse getCurrentUser() {
        return iUserService.getCurrentUserService();
    }

    @Override
    public void chageOwnPasswordService(ChangePasswordRequest request) {
        iUserService.changeOwnPasswordService(request);
    }
}
