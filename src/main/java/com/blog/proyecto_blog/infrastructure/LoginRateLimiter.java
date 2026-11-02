package com.blog.proyecto_blog.infrastructure;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class LoginRateLimiter {

    private static final long MAXIMUM_CLIENTS = 10_000;

    private final LoginRateLimitProperties properties;
    private final Cache<String, Bucket> buckets;

    public LoginRateLimiter(LoginRateLimitProperties properties) {
        this.properties = properties;
        this.buckets = Caffeine.newBuilder()
                .maximumSize(MAXIMUM_CLIENTS)
                .expireAfterAccess(properties.window().multipliedBy(2))
                .build();
    }

    public ConsumptionProbe consume(String clientKey) {
        Bucket bucket = buckets.get(clientKey, ignored -> newBucket());

        return bucket.tryConsumeAndReturnRemaining(1);
    }

    private Bucket newBucket() {
        return Bucket.builder()
                .addLimit(limit -> limit
                        .capacity(properties.maxAttempts())
                        .refillIntervally(
                                properties.maxAttempts(),
                                properties.window()
                        )
                )
                .build();
    }
}