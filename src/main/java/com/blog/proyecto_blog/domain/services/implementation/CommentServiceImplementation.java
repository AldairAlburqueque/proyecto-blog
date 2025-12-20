package com.blog.proyecto_blog.domain.services.implementation;

import com.blog.proyecto_blog.application.usescases.dto.request.CommentRequest;
import com.blog.proyecto_blog.application.usescases.dto.request.CommentUpdateRequest;
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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no autenticado"));

        BlogEntity blog = blogRepository.findById(request.getBlogId())
                .orElseThrow(() -> new RuntimeException("Blog no encontrado"));

        CommentEntity entity = commentMapper.toEntity(request, user, blog);

        CommentEntity saved = commentRepository.save(entity);

        return commentMapper.toResponse(saved);
    }

    @Override
    public CommentSimpleResponse updateCommentService(Long id, CommentUpdateRequest request) {
        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        UserEntity currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no autenticado"));

        CommentEntity comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comentario no encontrado"));

        if (!comment.getUser().getIdUser().equals(currentUser.getIdUser())) {
            throw new RuntimeException("No tienes permiso para editar este comentario");
        }

        comment.setComment(request.getComment());

        CommentEntity updated = commentRepository.save(comment);

        return commentMapper.toResponse(updated);
    }

    @Override
    public void deleteCommentService(Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        UserEntity currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no autenticado"));

        CommentEntity comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comentario no encontrado"));

        boolean isCommentOwner =
                comment.getUser().getIdUser().equals(currentUser.getIdUser());

        boolean isBlogOwner =
                comment.getBlog().getUser().getIdUser().equals(currentUser.getIdUser());

        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_Admin"));

        if (!isCommentOwner && !isBlogOwner && !isAdmin) {
            throw new AccessDeniedException("No tienes permiso para eliminar este comentario");
        }

        commentRepository.delete(comment);
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
