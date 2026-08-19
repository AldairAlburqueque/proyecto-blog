package com.blog.proyecto_blog.infrastructure.configuration.security;

import com.blog.proyecto_blog.infrastructure.database.entity.CategoryEntity;
import com.blog.proyecto_blog.infrastructure.database.entity.RolEntity;
import com.blog.proyecto_blog.infrastructure.database.repositories.CategoryRepository;
import com.blog.proyecto_blog.infrastructure.database.repositories.RolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final RolRepository rolRepository;
    private final CategoryRepository categoryRepository;

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            createRoles();
            createCategories();
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
}
