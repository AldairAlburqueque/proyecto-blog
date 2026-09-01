package com.blog.proyecto_blog.infrastructure.configuration.security;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.crypto.SecretKey;
import java.time.Duration;

//con los not hace q la app no arranque si falte un valor importante
@Validated
//conecta propiedades YAML con las campos de record
@ConfigurationProperties(prefix = "security.jwt")
public record JwtProperties (
        @NotBlank String secret,
        @NotBlank String issuer,
        @NotNull Duration accessTokenTtl
){
    public SecretKey signingKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
