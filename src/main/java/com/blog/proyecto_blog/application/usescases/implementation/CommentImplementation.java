package com.blog.proyecto_blog.application.usescases.implementation;

import com.blog.proyecto_blog.application.usescases.dto.request.CommentRequest;
import com.blog.proyecto_blog.application.usescases.dto.response.CommentSimpleResponse;
import com.blog.proyecto_blog.application.usescases.interfaces.ICommentInterface;
import com.blog.proyecto_blog.domain.services.interfaces.ICommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentImplementation implements ICommentInterface {
    private final ICommentService iCommentService;

    @Override
    public CommentSimpleResponse createCommnet(CommentRequest request) {
        return iCommentService.createCommnetService(request);
    }

    @Override
    public CommentSimpleResponse updateComment(Long id, CommentRequest request) {
        return iCommentService.updateCommentService(id, request);
    }

    @Override
    public void deleteComment(Long id) {
        iCommentService.deleteCommentService(id);
    }

    @Override
    public List<CommentSimpleResponse> getAllComments() {
        return iCommentService.getAllCommentsService();
    }

    @Override
    public List<CommentSimpleResponse> getCommentsByIdBlog(Long blogId) {
        return iCommentService.getCommentsByIdBlogService(blogId);
    }
}
