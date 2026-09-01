package com.blog.proyecto_blog.infrastructure.configuration.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.bootstrap-admin")
public record BootstrapAdminProperties (
    boolean enabled,
    String name,
    String email,
    String password
){
}
