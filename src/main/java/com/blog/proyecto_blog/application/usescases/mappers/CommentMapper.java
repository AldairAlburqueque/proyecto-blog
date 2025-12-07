package com.blog.proyecto_blog.application.usescases.mappers;

import com.blog.proyecto_blog.application.usescases.dto.request.CommentRequest;
import com.blog.proyecto_blog.application.usescases.dto.response.CommentResponse;
import com.blog.proyecto_blog.application.usescases.dto.response.CommentSimpleResponse;
import com.blog.proyecto_blog.infrastructure.database.entity.BlogEntity;
import com.blog.proyecto_blog.infrastructure.database.entity.CommentEntity;
import com.blog.proyecto_blog.infrastructure.database.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentMapper {

    private final UserMapper userMapper;

    public CommentEntity toEntity(CommentRequest request, UserEntity user, BlogEntity blog) {
        if (request == null) return null;

        CommentEntity entity = new CommentEntity();
        entity.setComment(request.getComment());
        entity.setUser(user);
        entity.setBlog(blog);

        return entity;
    }

    public CommentSimpleResponse toResponse(CommentEntity entity) {
        if (entity == null) return null;

        CommentSimpleResponse response = new CommentSimpleResponse();
        response.setIdComment(entity.getIdComment());
        response.setComment(entity.getComment());
        //response.setState(entity.getState().name());
        response.setBlogId(entity.getBlog().getIdBlog());
        response.setCreatedAt(entity.getCreatedAt());
        response.setUser(userMapper.toSimple(entity.getUser()));

        return response;
    }

}
