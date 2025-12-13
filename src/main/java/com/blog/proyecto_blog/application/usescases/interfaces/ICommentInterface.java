package com.blog.proyecto_blog.application.usescases.interfaces;

import com.blog.proyecto_blog.application.usescases.dto.request.CommentRequest;
import com.blog.proyecto_blog.application.usescases.dto.request.CommentUpdateRequest;
import com.blog.proyecto_blog.application.usescases.dto.response.CommentSimpleResponse;

import java.util.List;

public interface ICommentInterface {
    CommentSimpleResponse createCommnet(CommentRequest request);
    CommentSimpleResponse updateComment(Long id, CommentUpdateRequest request);
    void deleteComment(Long id);
    List<CommentSimpleResponse> getAllComments();
    List<CommentSimpleResponse> getCommentsByIdBlog(Long blogId);
}
