package com.blog.proyecto_blog.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.bucket4j.ConsumptionProbe;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class LoginRateLimitFilter extends OncePerRequestFilter {

    private static final String LOGIN_PATH = "/auth/login";

    private final LoginRateLimiter loginRateLimiter;
    private final ObjectMapper objectMapper;

    public LoginRateLimitFilter(
            LoginRateLimiter loginRateLimiter,
            ObjectMapper objectMapper
    ) {
        this.loginRateLimiter = loginRateLimiter;
        this.objectMapper = objectMapper;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !(
                "POST".equalsIgnoreCase(request.getMethod())
                && LOGIN_PATH.equals(request.getServletPath())
                );
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String clientIp = request.getRemoteAddr();

        ConsumptionProbe probe = loginRateLimiter.consume(clientIp);

        if (probe.isConsumed()) {
            filterChain.doFilter(request, response);
            return;
        }

        long retryAfterSeconds = Math.max(
                1,
                TimeUnit.NANOSECONDS.toSeconds(
                        probe.getNanosToWaitForRefill()
                )
        );

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.TOO_MANY_REQUESTS,
                "Demasiados intentos de inicio de sesión. Intenta nuevamente más tarde."
        );
        problem.setTitle("Límite de intentos excedido");

        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        response.setContentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE);
        response.setHeader(
                HttpHeaders.RETRY_AFTER,
                String.valueOf(retryAfterSeconds)
        );

        objectMapper.writeValue(response.getOutputStream(), problem);
    };
}
