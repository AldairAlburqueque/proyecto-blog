package com.blog.proyecto_blog.application.usescases.mappers;

import com.blog.proyecto_blog.application.usescases.dto.request.UserRequest;
import com.blog.proyecto_blog.application.usescases.dto.response.UserResponse;
import com.blog.proyecto_blog.application.usescases.dto.response.UserSimpleResponse;
import com.blog.proyecto_blog.infrastructure.database.entity.RolEntity;
import com.blog.proyecto_blog.infrastructure.database.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final RoleMapper roleMapper;


    // UserEntity
    public UserEntity toEntity(UserRequest request, RolEntity rol) {
        if (request == null) return null;

        UserEntity entity = new UserEntity();
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setEmail(request.getEmail());
        entity.setPassword(request.getPassword());
        entity.setRol(rol);

        return entity;
    }

    // UserResponse
    public UserResponse toResponse(UserEntity entity) {
        if (entity == null) return null;

        UserResponse response = new UserResponse();
        response.setIdUser(entity.getIdUser());
        response.setName(entity.getName());
        response.setEmail(entity.getEmail());
        response.setDescription(entity.getDescription());
        //response.setRol(roleMapper.toResponse(entity.getRol());
        response.setRol(entity.getRol().getRol());
        //response.setRolId(entity.getRol().getIdRol());

        return response;
    }


    // UserSimpleResponse
    public UserSimpleResponse toSimple(UserEntity entity) {
        if (entity == null) return null;

        UserSimpleResponse response = new UserSimpleResponse();
        response.setIdUser(entity.getIdUser());
        response.setName(entity.getName());

        return response;
    }
}
