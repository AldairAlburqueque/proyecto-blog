package com.blog.proyecto_blog.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter.ReferrerPolicy;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.config.http.SessionCreationPolicy;
import jakarta.servlet.DispatcherType;

import java.util.Arrays;
import com.fasterxml.jackson.databind.ObjectMapper;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class WebSecurityCongif {

    private final JWTAuthorizationFilter authorizationFilter;
    private final CorsProperties corsProperties;
    private final LoginRateLimiter loginRateLimiter;
    private final ObjectMapper objectMapper;

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .headers(headers -> headers
                        .contentSecurityPolicy(csp -> csp.policyDirectives(
                                "default-src 'none'; " +
                                        "base-uri 'none'; " +
                                        "form-action 'none'; " +
                                        "frame-ancestors 'none'"
                        ))
                        .referrerPolicy(referrer -> referrer.policy(
                                ReferrerPolicy.NO_REFERRER
                        ))
                        .permissionsPolicyHeader(permissions -> permissions.policy(
                                "camera=(), microphone=(), geolocation=()"
                        ))
                )
                .sessionManagement(sesion ->
                        sesion.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint((request, response, exception) ->
                                response.sendError(
                                        HttpServletResponse.SC_UNAUTHORIZED,
                                        "Authenticacion requerida"
                                )
                        )
                        .accessDeniedHandler((request, response, exception) ->
                                response.sendError(
                                        HttpServletResponse.SC_FORBIDDEN,
                                        "No tienes permisos para realizar esta accion"
                                )
                        )
                )
                .authorizeHttpRequests(auth -> auth
                                .dispatcherTypeMatchers(
                                        DispatcherType.ERROR,
                                        DispatcherType.FORWARD
                                ).permitAll()
                        // 1. PERMITIR TODAS LAS PETICIONES OPTIONS (CORS PREFLIGHT)
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // Endpoints públicos
                        .requestMatchers(HttpMethod.POST,
                                "/auth/login",
                                "/auth/save").permitAll()
                        .requestMatchers(HttpMethod.GET,
                                "/blog/list",
                                "/category/list"
                        ).permitAll()

                                .requestMatchers(HttpMethod.GET, "/user/me")
                                .authenticated()

                                .requestMatchers(HttpMethod.GET, "/user/**")
                                .hasRole("Admin")

                                .requestMatchers(HttpMethod.POST, "/category/save")
                                .hasRole("Admin")

                                .requestMatchers(HttpMethod.PUT, "/category/update/**")
                                .hasRole("Admin")

                                .requestMatchers(HttpMethod.DELETE, "/category/delete/**")
                                .hasRole("Admin")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(
                        new LoginRateLimitFilter(
                                loginRateLimiter,
                                objectMapper
                        ),
                        UsernamePasswordAuthenticationFilter.class
                )
                .addFilterAfter(
                        authorizationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(corsProperties.allowedOrigins());
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(false);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
