package com.blog.proyecto_blog.infrastructure;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;

@Validated
@ConfigurationProperties(prefix = "security.rate-limit.login")
public record LoginRateLimitProperties(
        @Min(1) long maxAttempts,
        @NotNull Duration   window
        ) {

}
