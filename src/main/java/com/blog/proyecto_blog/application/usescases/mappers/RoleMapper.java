package com.blog.proyecto_blog.application.usescases.mappers;

import com.blog.proyecto_blog.application.usescases.dto.response.RolResponse;
import com.blog.proyecto_blog.infrastructure.database.entity.RolEntity;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper {

    //RolEntity -> RolResponse
    public RolResponse toResponse(RolEntity entity) {
        if (entity == null) return null;

        RolResponse dto = new RolResponse();
        dto.setIdRol(entity.getIdRol());
        dto.setRol(entity.getRol());

        return dto;
    }
}
