package com.blog.proyecto_blog.domain.services.implementation;

import com.blog.proyecto_blog.application.usescases.dto.request.LoginRequest;
import com.blog.proyecto_blog.application.usescases.dto.request.RegisterUserRequest;
import com.blog.proyecto_blog.application.usescases.dto.response.LoginResponse;
import com.blog.proyecto_blog.application.usescases.dto.response.UserResponse;
import com.blog.proyecto_blog.application.usescases.mappers.UserMapper;
import com.blog.proyecto_blog.domain.exceptions.InvalidCredentialsException;
import com.blog.proyecto_blog.domain.services.interfaces.IAuthService;
import com.blog.proyecto_blog.infrastructure.configuration.security.JWTAuthenticationConfig;
import com.blog.proyecto_blog.infrastructure.database.entity.RolEntity;
import com.blog.proyecto_blog.infrastructure.database.entity.UserEntity;
import com.blog.proyecto_blog.infrastructure.database.repositories.RolRepository;
import com.blog.proyecto_blog.infrastructure.database.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.blog.proyecto_blog.domain.exceptions.EmailAlreadyInUseException;

import java.util.Locale;


@Service
@AllArgsConstructor
public class AuthServiceImplementacion implements IAuthService {

    private final UserRepository userRepository;
    private final JWTAuthenticationConfig jwtConfig;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final RolRepository rolRepository;

    private final AuthenticationManager authenticationManager;

    @Override
    public LoginResponse loginService(LoginRequest request) {

        String normalizedEmail = request.getEmail()
                .trim()
                .toLowerCase(Locale.ROOT);

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            normalizedEmail,
                            request.getPassword()
                    )
            );
        } catch (AuthenticationException exception) {
            throw new InvalidCredentialsException();
        }

        UserEntity user = userRepository.findByEmail(normalizedEmail)
                .orElseThrow(InvalidCredentialsException::new);

        //Verificamos contraseña si es correcta
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Email o contraseña incorrecta");
        }

        String token = jwtConfig.getJWTToken(user);

        return new LoginResponse(
                user.getIdUser(),
                token,
                user.getEmail(),
                user.getName(),
                user.getRol().getRol()

        );
    }

    @Override
    public UserResponse createUserServices(RegisterUserRequest request) {

        String normalizedEmail = request.getEmail()
                .trim()
                .toLowerCase(Locale.ROOT);

        if (userRepository.findByEmail(normalizedEmail).isPresent()) {
            throw new EmailAlreadyInUseException();
        }

        RolEntity userRol = rolRepository.findByRol("User")
              .orElseThrow(() -> new RuntimeException("El rol no existe"));

       UserEntity entity = userMapper.toEntity(request, userRol);
       entity.setEmail(normalizedEmail);
       entity.setPassword(passwordEncoder.encode(request.getPassword()));
       UserEntity saved = userRepository.save(entity);
       return userMapper.toResponse(saved);
    }
}
