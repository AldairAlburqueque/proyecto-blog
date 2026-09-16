package com.blog.proyecto_blog.domain.services.implementation;

import com.blog.proyecto_blog.application.usescases.dto.request.ChangePasswordRequest;
import com.blog.proyecto_blog.application.usescases.dto.request.UpdateProfileRequest;
//import com.blog.proyecto_blog.application.usescases.dto.request.UserRequest;
import com.blog.proyecto_blog.application.usescases.dto.response.UserResponse;
import com.blog.proyecto_blog.application.usescases.mappers.UserMapper;
import com.blog.proyecto_blog.domain.exceptions.InvalidPasswordChangeException;
import com.blog.proyecto_blog.domain.services.interfaces.IUserService;

import com.blog.proyecto_blog.infrastructure.database.entity.UserEntity;

import com.blog.proyecto_blog.infrastructure.database.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class UserServiceImplementation implements IUserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

//    @Override
//    public UserResponse updateUserServices(Long id, UserRequest request) {
//        Authentication authentication =
//                SecurityContextHolder.getContext().getAuthentication();
//
//        UserEntity currentUser = userRepository
//                .findByEmail(authentication.getName())
//                .orElseThrow(() ->
//                        new RuntimeException("Usuario no autenticado")
//                );
//
//        UserEntity userToUpdate = userRepository.findById(id)
//                .orElseThrow(() ->
//                        new RuntimeException("Usuario no encontrado")
//                );
//
//        boolean isOwner = currentUser.getIdUser()
//                .equals(userToUpdate.getIdUser());
//
//
//        if (!isOwner) {
//            throw new AccessDeniedException(
//                    "No tienes permiso para editar este usuario"
//            );
//        }
//
//        userToUpdate.setName(request.getName());
//        userToUpdate.setEmail(request.getEmail());
//        userToUpdate.setDescription(request.getDescription());
//
//        if (request.getPassword() != null
//                && !request.getPassword().isBlank()) {
//            userToUpdate.setPassword(
//                    passwordEncoder.encode(request.getPassword())
//            );
//        }
//
//        UserEntity updated = userRepository.save(userToUpdate);
//
//        return userMapper.toResponse(updated);
//    }

    @Override
    public UserResponse updateOwnerProfileService(UpdateProfileRequest request) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        UserEntity currentUser = userRepository
                .findByEmail(authentication.getName())
                .orElseThrow(() ->
                        new RuntimeException("Usuario no autenticado"));

        currentUser.setName(request.getName());
        currentUser.setDescription(request.getDescription());

        UserEntity updatedUser = userRepository.save(currentUser);

        return userMapper.toResponse(updatedUser);
    }

    @Override
    public UserResponse getUserByIdServices(Long id) {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        UserEntity currentUser = userRepository
                .findByEmail(authentication.getName())
                .orElseThrow(() ->
                        new RuntimeException("Usuario no autenticado")
                );

        UserEntity requestedUser = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Usuario no encontrado")
                );

        boolean isOwner = currentUser.getIdUser()
                .equals(requestedUser.getIdUser());

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(authority ->
                        authority.getAuthority().equals("ROLE_Admin")
                );

        if (!isOwner && !isAdmin) {
            throw new AccessDeniedException(
                    "No tienes permiso para ver este perfil"
            );
        }

        return userMapper.toResponse(requestedUser);
    }

    @Override
    public List<UserResponse> getAllUsersServices() {
        Iterable<UserEntity> users = userRepository.findAll();

        return StreamSupport.stream(users.spliterator(), false)
                .map(userMapper::toResponse)
                .toList();
    }

    @Override
    public void deleteUserServices(Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        UserEntity currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no autenticado"));

        UserEntity userToDelete = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        boolean isSelf = currentUser.getIdUser().equals(userToDelete.getIdUser());

        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_Admin"));

        if (!isSelf && !isAdmin) {
            throw new AccessDeniedException("No tienes permiso para eliminar este usuario");
        }

        userRepository.delete(userToDelete);
    }

    @Override
    public UserResponse getCurrentUserService() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        UserEntity currentUser = userRepository
                .findByEmail(authentication.getName())
                .orElseThrow(() ->
                        new RuntimeException("Usuario no autenticado"));

        return userMapper.toResponse(currentUser);
    }

    @Override
    public void changeOwnPasswordService(ChangePasswordRequest request) {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        UserEntity currentUser = userRepository
                .findByEmail(authentication.getName())
                .orElseThrow(InvalidPasswordChangeException::new);

        boolean currentPasswordMatches = passwordEncoder.matches(
                request.getCurrentPassword(),
                currentUser.getPassword()
        );

        boolean isSamePassword = passwordEncoder.matches(
                request.getNewPassword(),
                currentUser.getPassword()
        );

        if (!currentPasswordMatches || isSamePassword) {
            throw new InvalidPasswordChangeException();
        }

        currentUser.setPassword(
                passwordEncoder.encode(request.getNewPassword())
        );

        userRepository.save(currentUser);
    }
}
