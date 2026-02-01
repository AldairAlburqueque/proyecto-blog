package com.blog.proyecto_blog.domain.services.implementation;

import com.blog.proyecto_blog.application.usescases.dto.request.LoginRequest;
import com.blog.proyecto_blog.application.usescases.dto.request.UserRequest;
import com.blog.proyecto_blog.application.usescases.dto.response.LoginResponse;
import com.blog.proyecto_blog.application.usescases.dto.response.UserResponse;
import com.blog.proyecto_blog.application.usescases.interfaces.IUserInterface;
import com.blog.proyecto_blog.application.usescases.mappers.UserMapper;
import com.blog.proyecto_blog.domain.services.interfaces.IAuthService;
import com.blog.proyecto_blog.infrastructure.configuration.security.JWTAuthenticationConfig;
import com.blog.proyecto_blog.infrastructure.database.entity.RolEntity;
import com.blog.proyecto_blog.infrastructure.database.entity.UserEntity;
import com.blog.proyecto_blog.infrastructure.database.repositories.RolRepository;
import com.blog.proyecto_blog.infrastructure.database.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class AuthServiceImplementacion implements IAuthService {

    private final UserRepository userRepository;
    private final JWTAuthenticationConfig jwtConfig;
    private final PasswordEncoder passwordEncoder;
    private final IUserInterface userInterface;
    private final UserMapper userMapper;
    private final RolRepository rolRepository;

    @Override
    public LoginResponse loginService(LoginRequest request) {
        UserEntity user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Email o contraseña incorrecta"));

        //Verificamos contraseña si es correcta
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Email o contraseña incorrecta");
        }

        String token = jwtConfig.getJWTToken(user);

        return new LoginResponse(
                token,
                user.getEmail(),
                user.getName(),
                user.getRol().getRol()
        );
    }

    @Override
    public UserResponse createUserServices(UserRequest request) {
        RolEntity rolEntity = rolRepository.findById(request.getRolId())
              .orElseThrow(() -> new RuntimeException("El rol no existe"));

       UserEntity entity = userMapper.toEntity(request, rolEntity);

       entity.setPassword(passwordEncoder.encode(request.getPassword()));

       UserEntity saved = userRepository.save(entity);

        return userMapper.toResponse(saved);
    }
}
