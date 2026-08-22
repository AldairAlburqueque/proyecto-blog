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

import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final RolRepository rolRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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
        if (!userRepository.existsByRol_Rol("Admin")) {

            RolEntity adminRol = rolRepository.findByRol("Admin")
                    .orElseThrow(() -> new RuntimeException("El rol Admin no existe."));

            UserEntity admin = new UserEntity();
            admin.setName("Super Admin");
            admin.setEmail("admin@gmail.com");
            admin.setDescription("Solo se crea el super admin");
            admin.setPassword(
                    passwordEncoder.encode("123456")
            );
            admin.setRol(adminRol);

            userRepository.save(admin);
        }
    }
}
