package com.blog.proyecto_blog.configuration.security;

import com.blog.proyecto_blog.infrastructure.database.entity.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

import static com.blog.proyecto_blog.configuration.security.Constans.*;


@Component
public class JWTAuthenticationConfig {

    public String getJWTToken(UserEntity user) {
        List<GrantedAuthority> authorities =
                AuthorityUtils.commaSeparatedStringToAuthorityList(
                        "ROLE_" + user.getRol().getRol()
                );

        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("idUser", user.getIdUser())
                .claim("name", user.getName())
                .claim("role", user.getRol().getRol())
                .claim("authorities", authorities.stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION_TIME))
                .signWith(getSigningKey(SUPER_SECRET_KEY))
                .compact();
    }


    public Claims decodeToken(String token) {
        return Jwts.parser()
                .setSigningKey(getSigningKey(SUPER_SECRET_KEY))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}

