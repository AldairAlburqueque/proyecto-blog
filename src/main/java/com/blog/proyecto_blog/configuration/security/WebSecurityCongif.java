package com.blog.proyecto_blog.configuration.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class WebSecurityCongif {
    private final JWTAuthorizationFilter authorizationFilter;

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable) // Deshabilitamos CSRF para API REST
                .authorizeHttpRequests(auth -> auth
                        // Endpoints públicos
                        .requestMatchers(HttpMethod.POST, "/auth/login", "/user/save").permitAll()
                        .requestMatchers(HttpMethod.GET, "/blog/list").permitAll() // ejemplo para blogs públicos

                        // Endpoints de administración
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        // Cualquier otro endpoint requiere autenticación
                        .anyRequest().authenticated()
                )
                // Agregamos el filtro JWT después de UsernamePasswordAuthenticationFilter
                .addFilterAfter(authorizationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
