package com.blog.proyecto_blog.infrastructure;

import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

class LoginRateLimiterTest {

    @Test
    void shouldRejectAttemptAfterMaximumIsReached() {
        LoginRateLimitProperties properties =
                new LoginRateLimitProperties(2, Duration.ofHours(1));

        LoginRateLimiter rateLimiter =
                new LoginRateLimiter(properties);

        assertThat(rateLimiter.consume("127.0.0.1").isConsumed()).isTrue();
        assertThat(rateLimiter.consume("127.0.0.1").isConsumed()).isTrue();
        assertThat(rateLimiter.consume("127.0.0.1").isConsumed()).isFalse();
    }
}
