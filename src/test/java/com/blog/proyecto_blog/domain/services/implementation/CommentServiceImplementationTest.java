package com.blog.proyecto_blog.domain.services.implementation;

import com.blog.proyecto_blog.application.usescases.dto.request.CommentRequest;
import com.blog.proyecto_blog.application.usescases.mappers.CommentMapper;
import com.blog.proyecto_blog.domain.exceptions.InvalidContentException;
import com.blog.proyecto_blog.domain.exceptions.InvalidCredentialsException;
import com.blog.proyecto_blog.infrastructure.UserContentSanitizer;
import com.blog.proyecto_blog.infrastructure.database.entity.BlogEntity;
import com.blog.proyecto_blog.infrastructure.database.entity.CommentEntity;
import com.blog.proyecto_blog.infrastructure.database.entity.UserEntity;
import com.blog.proyecto_blog.infrastructure.database.repositories.BlogRepository;
import com.blog.proyecto_blog.infrastructure.database.repositories.CommentRepository;
import com.blog.proyecto_blog.infrastructure.database.repositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;


import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentServiceImplementationTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BlogRepository blogRepository;

    @Mock
    private CommentMapper commentMapper;

    @Mock
    private UserContentSanitizer userContentSanitizer;

    @InjectMocks
    private CommentServiceImplementation commentService;

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldRejectCommentWhenSanitizedContentIsBlank() {
        //Arrange
        String email = "user@blog.com";
        CommentRequest request = new CommentRequest(
                "<img src=x onerror=alert(1)>",
                3L
        );

        UserEntity user = new UserEntity();
        BlogEntity blog = new BlogEntity();
        CommentEntity comment = new CommentEntity();

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(email, null)
        );

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(blogRepository.findById(3L)).thenReturn(Optional.of(blog));
        when(commentMapper.toEntity(request, user, blog)).thenReturn(comment);

        //Simulamos que sanitizador eliminó todo el contenido.
        when(userContentSanitizer.sanitizerPlainText(request.getComment()))
                .thenReturn("");

        //Act + Assert
        assertThatThrownBy(() -> commentService.createCommnetService(request))
                .isInstanceOf(InvalidContentException.class);

        //El punto de seguridad mas importante: no persistir contenido invalido.
        verify(commentRepository, never()).save(any());
    }


    @Test
    void shouldNotDeleteCommentWhenUserIsNotOwnerAdminOrBlogOwner() {
        //Arrange
        String email = "user@blog.com";

        UserEntity currentUser = new UserEntity();
        currentUser.setIdUser(1L);

        UserEntity commentOwner = new UserEntity();
        commentOwner.setIdUser(2L);

        UserEntity blogOwner = new UserEntity();
        blogOwner.setIdUser(3L);

        BlogEntity blog = new BlogEntity();
        blog.setUser(blogOwner);

        CommentEntity comment = new CommentEntity();
        comment.setUser(commentOwner);
        comment.setBlog(blog);

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        email,
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_User"))
                )
        );

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(currentUser));

        when(commentRepository.findById(10L))
                .thenReturn(Optional.of(comment));

        //Act + Assert
        assertThatThrownBy(() -> commentService.deleteCommentService(10L))
                .isInstanceOf(AccessDeniedException.class);

        verify(commentRepository, never()).delete(any());
    }

    @Test
    void shouldDeleteCommentWhenUserIsAdmin() {
        //Arrange
        String email = "admin@blog.com";

        UserEntity admin = new UserEntity();
        admin.setIdUser(1L);

        UserEntity commentOwner = new UserEntity();
        commentOwner.setIdUser(2L);

        UserEntity blogOwner = new UserEntity();
        blogOwner.setIdUser(3L);

        BlogEntity blog = new BlogEntity();
        blog.setUser(blogOwner);

        CommentEntity comment = new CommentEntity();
        comment.setUser(commentOwner);
        comment.setBlog(blog);

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        email,
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_Admin"))
                )
        );

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(admin));

        when(commentRepository.findById(10L))
                .thenReturn(Optional.of(comment));

        //Act
        commentService.deleteCommentService(10L);

        //Assert
        verify(commentRepository).delete(comment);
    }

    @Test
    void shouldRejectRequestWhenAuthenticatedUserNoLongerExists() {
        //Arrange
        String email = "deleted-user@blog.com";
        CommentRequest request = new CommentRequest(
                "Comentario válido",
                3L
        );

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(email, null)
        );

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.empty());

        //Act + Assert
        assertThatThrownBy(() -> commentService.createCommnetService(request))
                .isInstanceOf(InvalidCredentialsException.class);

        verifyNoInteractions(
                blogRepository,
                commentMapper,
                userContentSanitizer,
                commentRepository
        );
    }
}
