package com.blog.proyecto_blog.infrastructure.configuration.security;

import com.blog.proyecto_blog.infrastructure.database.entity.CategoryEntity;
import com.blog.proyecto_blog.infrastructure.database.entity.RolEntity;
import com.blog.proyecto_blog.infrastructure.database.entity.UserEntity;
import com.blog.proyecto_blog.infrastructure.database.repositories.CategoryRepository;
import com.blog.proyecto_blog.infrastructure.database.repositories.RolRepository;
import com.blog.proyecto_blog.infrastructure.database.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final RolRepository rolRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final BootstrapAdminProperties bootstrapAdminProperties;

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            createRoles();
            createCategories();
            createAdmin();
        };
    }

    public void createRoles() {
            if (!rolRepository.existsByRol("Admin")) {
                RolEntity admin = new RolEntity();
                admin.setRol("Admin");
                rolRepository.save(admin);
            }
            if (!rolRepository.existsByRol("User")) {
                RolEntity user = new RolEntity();
                user.setRol("User");
                rolRepository.save(user);
            }
    }

    public void createCategories() {
        Map<String, String> categories = Map.of(
                "Java", "Mundo Java, JVM y buenas prácticas.",
                "Spring Boot", "Desarrollo de microservicios y APIs REST.",
                "React", "Construcción de interfaces modernas con frontend.",
                "Docker", "Contenedores, despliegue e infraestructura.",
                "JavaScript", "Ecosistema web, Node.js y ES6+."
        );
        categories.forEach((name, description) -> {
            if (!categoryRepository.existsByCategoria(name)) {
                CategoryEntity entity = new CategoryEntity();
                entity.setCategoria(name);
                entity.setDescription(description);
                categoryRepository.save(entity);
            }
        });
    }

    public void createAdmin() {
        if (!bootstrapAdminProperties.enabled()) {
            return;
        }

        if (!StringUtils.hasText(bootstrapAdminProperties.name())
                || !StringUtils.hasText(bootstrapAdminProperties.email())
                || !StringUtils.hasText(bootstrapAdminProperties.password())) {
            throw new IllegalStateException(
                    "El bootstrap de administrador está habilitado, "
                            + "pero faltan sus variables de configuración"
            );
        }

        if (userRepository.existsByRol_Rol("Admin")) {
            return;
        }

        RolEntity adminRol = rolRepository.findByRol("Admin")
                .orElseThrow(() ->
                        new IllegalStateException("El rol Admin no existe")
                );

        UserEntity admin = new UserEntity();
        admin.setName(bootstrapAdminProperties.name());
        admin.setEmail(bootstrapAdminProperties.email());
        admin.setDescription("Administrador inicial del sistema");
        admin.setPassword(
                passwordEncoder.encode(bootstrapAdminProperties.password())
        );
        admin.setRol(adminRol);

        userRepository.save(admin);
    }
}
