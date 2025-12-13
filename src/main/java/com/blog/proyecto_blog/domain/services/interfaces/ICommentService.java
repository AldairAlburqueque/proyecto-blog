package com.blog.proyecto_blog.domain.services.interfaces;

import com.blog.proyecto_blog.application.usescases.dto.request.CommentRequest;
import com.blog.proyecto_blog.application.usescases.dto.request.CommentUpdateRequest;
import com.blog.proyecto_blog.application.usescases.dto.response.CommentSimpleResponse;

import java.util.List;

public interface ICommentService {
    CommentSimpleResponse createCommnetService(CommentRequest request);
    CommentSimpleResponse updateCommentService(Long id, CommentUpdateRequest request);//actualizado CommentRequest
    void deleteCommentService(Long id);
    List<CommentSimpleResponse> getAllCommentsService();
    List<CommentSimpleResponse> getCommentsByIdBlogService(Long blogId);
}
