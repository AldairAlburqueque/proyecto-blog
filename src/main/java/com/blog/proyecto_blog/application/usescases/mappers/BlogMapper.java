package com.blog.proyecto_blog.application.usescases.mappers;

import com.blog.proyecto_blog.application.usescases.dto.request.BlogRequest;
import com.blog.proyecto_blog.application.usescases.dto.response.BlogResponse;
import com.blog.proyecto_blog.application.usescases.mappers.CategoryMapper;
import com.blog.proyecto_blog.application.usescases.mappers.CommentMapper;
import com.blog.proyecto_blog.application.usescases.mappers.UserMapper;
import com.blog.proyecto_blog.infrastructure.database.entity.BlogEntity;
import com.blog.proyecto_blog.infrastructure.database.entity.CategoryEntity;
import com.blog.proyecto_blog.infrastructure.database.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BlogMapper {

    private final UserMapper userMapper;
    private final CategoryMapper categoryMapper;
    private final CommentMapper commentMapper;

    public BlogEntity toEntity(BlogRequest request, UserEntity user, CategoryEntity category) {
        if (request == null) return null;

        BlogEntity entity = new BlogEntity();
        entity.setTitle(request.getTitle());
        entity.setContent(request.getContent());
        entity.setUser(user);
        entity.setCategory(category);

        return entity;
    }

    public BlogResponse toResponse(BlogEntity entity) {
        if (entity == null) return null;

        BlogResponse response = new BlogResponse();
        response.setIdBlog(entity.getIdBlog());
        response.setTitle(entity.getTitle());
        response.setContent(entity.getContent());
        response.setCreatedAt(entity.getCreatedAt());

        response.setUser(userMapper.toSimple(entity.getUser()));

        response.setCategory(categoryMapper.toResponse(entity.getCategory()));

        if (entity.getComments() != null) {
            response.setComments(
                    entity.getComments().stream()
                            .map(commentMapper::toResponse)
                            .toList()
            );
        }

        return response;
    }
}
