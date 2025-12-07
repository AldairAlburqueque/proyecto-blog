package com.blog.proyecto_blog.domain.services.implementation;

import com.blog.proyecto_blog.application.usescases.dto.request.UserRequest;
import com.blog.proyecto_blog.application.usescases.dto.response.UserResponse;
import com.blog.proyecto_blog.application.usescases.mappers.UserMapper;
import com.blog.proyecto_blog.domain.services.interfaces.IUserService;
import com.blog.proyecto_blog.infrastructure.database.entity.RolEntity;
import com.blog.proyecto_blog.infrastructure.database.entity.UserEntity;
import com.blog.proyecto_blog.infrastructure.database.repositories.RolRepository;
import com.blog.proyecto_blog.infrastructure.database.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class UserServiceImplementation implements IUserService {
    private final UserRepository userRepository;
    private final RolRepository rolRepository;
    private final UserMapper userMapper;


    @Override
    public UserResponse createUserServices(UserRequest request) {
        // 1️⃣ Buscar el rol
        RolEntity rolEntity = rolRepository.findById(request.getRolId())
                .orElseThrow(() -> new RuntimeException("El rol no existe"));

        // 2️⃣ Mapear request → entity
        UserEntity entity = userMapper.toEntity(request, rolEntity);

        // 3️⃣ Guardar en DB
        UserEntity saved = userRepository.save(entity);

        // 4️⃣ Convertir entity → response
        return userMapper.toResponse(saved);
    }

    @Override
    public UserResponse updateUserServices(Long id, UserRequest request) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        RolEntity rol = rolRepository.findById(request.getRolId())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        // actualizar valores
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setDescription(request.getDescription());
        user.setPassword(request.getPassword()); // luego lo encriptaremos
        user.setRol(rol);

        UserEntity updated = userRepository.save(user);

        return userMapper.toResponse(updated);
    }

    @Override
    public UserResponse getUserByIdServices(Long id) {
        UserEntity entity = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return userMapper.toResponse(entity);
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
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado");
        }

        userRepository.deleteById(id);
    }
}
