package com.blog.proyecto_blog.domain.services.implementation;

import com.blog.proyecto_blog.application.usescases.dto.request.BlogRequest;
import com.blog.proyecto_blog.application.usescases.mappers.BlogMapper;
import com.blog.proyecto_blog.infrastructure.UserContentSanitizer;
import com.blog.proyecto_blog.infrastructure.database.entity.BlogEntity;
import com.blog.proyecto_blog.infrastructure.database.entity.UserEntity;
import com.blog.proyecto_blog.infrastructure.database.repositories.BlogRepository;
import com.blog.proyecto_blog.infrastructure.database.repositories.CategoryRepository;
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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BlogServiceImplementationTest {

    @Mock
    private BlogRepository blogRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private BlogMapper blogMapper;

    @Mock
    private UserContentSanitizer userContentSanitizer;

    @InjectMocks
    private BlogServiceImplementation blogService;

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldNotUpdateBlogWhenUserIsOwner() {
        //Arrange
        String email = "other-user@blog.com";
        BlogRequest request = new BlogRequest(
                "Título actualizado",
                "Contenido actualizado",
                1L
        );

        UserEntity currentUser = new UserEntity();
        currentUser.setIdUser(1L);

        UserEntity blogOwner = new UserEntity();
        blogOwner.setIdUser(2L);

        BlogEntity blog = new BlogEntity();
        blog.setUser(blogOwner);

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(email, null)
        );

        when(blogRepository.findById(10L)).thenReturn(Optional.of(blog));
        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(currentUser));

        //Act + Assert
        assertThatThrownBy(() -> blogService.updateBlogService(10L, request))
                .isInstanceOf(AccessDeniedException.class);

        verify(blogRepository, never()).save(any());
    }

    @Test
    void shouldDeleteBlogWhenUserIsAdmin() {
        // Arrange
        String email = "admin@blog.com";

        UserEntity admin = new UserEntity();
        admin.setIdUser(1L);

        UserEntity blogOwner = new UserEntity();
        blogOwner.setIdUser(2L);

        BlogEntity blog = new BlogEntity();
        blog.setUser(blogOwner);

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        email,
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_Admin"))
                )
        );

        when(blogRepository.findById(10L)).thenReturn(Optional.of(blog));
        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(admin));

        // Act
        blogService.deleteBlogService(10L);

        // Assert
        verify(blogRepository).delete(blog);
    }
}
