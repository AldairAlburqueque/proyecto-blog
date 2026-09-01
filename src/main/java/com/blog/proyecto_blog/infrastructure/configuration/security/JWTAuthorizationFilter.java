package com.blog.proyecto_blog.infrastructure.configuration.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

import static com.blog.proyecto_blog.infrastructure.configuration.security.Constans.AUTHORIZATION_HEADER;
import static com.blog.proyecto_blog.infrastructure.configuration.security.Constans.TOKEN_BEARER_PREFIX;

@Component
@RequiredArgsConstructor
public class JWTAuthorizationFilter extends OncePerRequestFilter {

    private final JwtProperties jwtProperties;

    private Claims parseToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);
        String jwtToken = authorizationHeader.substring(TOKEN_BEARER_PREFIX.length());

        return Jwts.parser()
                .verifyWith(jwtProperties.signingKey())
                .requireIssuer(jwtProperties.issuer())
                .build()
                .parseSignedClaims(jwtToken)
                .getPayload();
    }

    private void setAuthentication(Claims claims) {
        List<String> authorities = claims.get("authorities", List.class);

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        claims.getSubject(),
                        null,
                        authorities.stream()
                                .map(SimpleGrantedAuthority::new)
                                .toList()
                );

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private boolean hasBearerToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);

        return authorizationHeader != null
                && authorizationHeader.startsWith(TOKEN_BEARER_PREFIX);
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException {

        try {
            if (hasBearerToken(request)) {
                Claims claims = parseToken(request);

                if (claims.get("authorities") != null) {
                    setAuthentication(claims);
                }
            }
        } catch (JwtException | IllegalArgumentException exception) {
            SecurityContextHolder.clearContext();
            response.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "Token inválido o expirado"
            );
            return;
        }

        chain.doFilter(request, response);
    }
}