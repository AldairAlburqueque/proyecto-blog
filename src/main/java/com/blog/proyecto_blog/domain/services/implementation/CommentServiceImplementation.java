package com.blog.proyecto_blog.domain.services.implementation;

import com.blog.proyecto_blog.application.usescases.dto.request.CommentRequest;
import com.blog.proyecto_blog.application.usescases.dto.response.CommentResponse;
import com.blog.proyecto_blog.application.usescases.dto.response.CommentSimpleResponse;
import com.blog.proyecto_blog.application.usescases.mappers.CommentMapper;
import com.blog.proyecto_blog.domain.services.interfaces.ICommentService;
import com.blog.proyecto_blog.infrastructure.database.entity.BlogEntity;
import com.blog.proyecto_blog.infrastructure.database.entity.CommentEntity;
import com.blog.proyecto_blog.infrastructure.database.entity.UserEntity;
import com.blog.proyecto_blog.infrastructure.database.repositories.BlogRepository;
import com.blog.proyecto_blog.infrastructure.database.repositories.CommentRepository;
import com.blog.proyecto_blog.infrastructure.database.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImplementation implements ICommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final BlogRepository blogRepository;
    private final CommentMapper commentMapper;

    @Override
    public CommentSimpleResponse createCommnetService(CommentRequest request) {
        UserEntity user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        BlogEntity blog = blogRepository.findById(request.getBlogId())
                .orElseThrow(() -> new RuntimeException("Blog no encontrado"));

        CommentEntity entity = commentMapper.toEntity(request, user, blog);

        CommentEntity saved = commentRepository.save(entity);

        return commentMapper.toResponse(saved);
    }

    @Override
    public CommentSimpleResponse updateCommentService(Long id, CommentRequest request) {
        CommentEntity entity = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comentario no encontrado"));

        // actualizar texto
        entity.setComment(request.getComment());

        // opcionalmente actualizar usuario
        if (request.getUserId() != null) {
            UserEntity user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new RuntimeException("User no encontrado"));
            entity.setUser(user);
        }

        CommentEntity updated = commentRepository.save(entity);

        return commentMapper.toResponse(updated);
    }

    @Override
    public void deleteCommentService(Long id) {
        if (!commentRepository.existsById(id)) {
            throw new RuntimeException("Comentario no encontrado");
        }
        commentRepository.deleteById(id);
    }

    @Override
    public List<CommentSimpleResponse> getAllCommentsService() {
        return commentRepository.findAll()
                .stream()
                .map(commentMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentSimpleResponse> getCommentsByIdBlogService(Long blogId) {
        List<CommentEntity> comments = commentRepository.findByBlog_IdBlog(blogId);
        return comments.stream()
                .map(commentMapper::toResponse)
                .toList();
    }
}
