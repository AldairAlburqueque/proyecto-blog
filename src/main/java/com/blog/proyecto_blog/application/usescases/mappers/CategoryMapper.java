package com.blog.proyecto_blog.application.usescases.mappers;

import com.blog.proyecto_blog.application.usescases.dto.request.CategoryRequest;
import com.blog.proyecto_blog.application.usescases.dto.response.CategoryResponse;
import com.blog.proyecto_blog.infrastructure.database.entity.CategoryEntity;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryEntity toEntity(CategoryRequest request) {
        if (request == null) return null;

        CategoryEntity entity = new CategoryEntity();
        entity.setCategoria(request.getCategoria());
        entity.setDescription(request.getDescription());
        return entity;
    }

    public CategoryResponse toResponse(CategoryEntity entity) {
        if (entity == null) return null;

        CategoryResponse response = new CategoryResponse();
        response.setIdCategory(entity.getIdCategory());
        response.setCategoria(entity.getCategoria());
        response.setDescription(entity.getDescription());
        return response;
    }
}
