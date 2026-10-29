package com.blog.proyecto_blog.domain.services.implementation;

import com.blog.proyecto_blog.application.usescases.mappers.UserMapper;
import com.blog.proyecto_blog.infrastructure.database.entity.UserEntity;
import com.blog.proyecto_blog.infrastructure.database.repositories.UserRepository;
import com.blog.proyecto_blog.application.usescases.dto.request.ChangePasswordRequest;
import com.blog.proyecto_blog.domain.exceptions.InvalidPasswordChangeException;

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
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplementationTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImplementation userService;

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldNotDeleteAnotherUserWhenCurrentUserIsNotAdmin() {
        //Arrange
        String email = "user@blog.com";

        UserEntity currentUser = new UserEntity();
        currentUser.setIdUser(1L);

        UserEntity userToDelete = new UserEntity();
        userToDelete.setIdUser(2L);

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        email,
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_User"))
                )
        );

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(currentUser));

        when(userRepository.findById(2L))
                .thenReturn(Optional.of(userToDelete));

        //Act + Assert
        assertThatThrownBy(() -> userService.deleteUserServices(2L))
                .isInstanceOf(AccessDeniedException.class);

        verify(userRepository, never()).delete(any());
    }

    @Test
    void shouldDeleteAnotherUserWhenCurrentUserIsAdmin() {
        //Arrange
        String email = "admin@blog.com";

        UserEntity admin = new UserEntity();
        admin.setIdUser(1L);

        UserEntity userToDelete = new UserEntity();
        userToDelete.setIdUser(2L);

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        email,
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_Admin"))
                )
        );

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(admin));

        when(userRepository.findById(2L))
                .thenReturn(Optional.of(userToDelete));

        //Act
        userService.deleteUserServices(2L);

        //Assert
        verify(userRepository).delete(userToDelete);
    }

    @Test
    void shouldNotChangePasswordWhenCurrentPasswordIsIncorrect() {
        // Arrange
        String email = "user@blog.com";
        String encodedPassword = "encoded-current-password";

        ChangePasswordRequest request = new ChangePasswordRequest(
                "wrong-current-password",
                "new-secure-password"
        );

        UserEntity currentUser = new UserEntity();
        currentUser.setPassword(encodedPassword);

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(email, null)
        );

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(currentUser));

        when(passwordEncoder.matches(
                request.getCurrentPassword(),
                encodedPassword
        )).thenReturn(false);

        // Act + Assert
        assertThatThrownBy(() -> userService.changeOwnPasswordService(request))
                .isInstanceOf(InvalidPasswordChangeException.class);

        verify(passwordEncoder, never()).encode(any());
        verify(userRepository, never()).save(any());
    }
}
