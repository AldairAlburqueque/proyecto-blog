package com.blog.proyecto_blog.infrastructure.configuration.security;

import com.blog.proyecto_blog.infrastructure.database.entity.UserEntity;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JWTAuthenticationConfig {

    private final JwtProperties jwtProperties;

    public String getJWTToken(UserEntity user) {
        List<GrantedAuthority> authorities =
                AuthorityUtils.commaSeparatedStringToAuthorityList(
                        "ROLE_" + user.getRol().getRol()
                );

        Instant now = Instant.now();

        return Jwts.builder()
                .subject(user.getEmail())
                .issuer(jwtProperties.issuer())
                .claim("idUser", user.getIdUser())
                .claim("name", user.getName())
                .claim("role", user.getRol().getRol())
                .claim("authorities", authorities.stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(jwtProperties.accessTokenTtl())))
                .signWith(jwtProperties.signingKey())
                .compact();
    }
}