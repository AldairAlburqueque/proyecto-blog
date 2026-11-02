package com.blog.proyecto_blog.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

class LoginRateLimitFilterTest {

    @Test
    void shouldReturnTooManyRequestsWhenLoginLimitIsExceeded()
        throws Exception {

        //Arrange: solo se permite un intento por hora.
        LoginRateLimitProperties properties =
                new LoginRateLimitProperties(1, Duration.ofHours(1));

        LoginRateLimiter rateLimiter =
                new LoginRateLimiter(properties);

        LoginRateLimitFilter filter =
                new LoginRateLimitFilter(
                        rateLimiter,
                        new ObjectMapper()
                );

        String clientIp = "127.0.0.1";

        //Consumimos el unico intentlo disponible.
        rateLimiter.consume(clientIp);

        MockHttpServletRequest request =
                new MockHttpServletRequest("POST", "/auth/login");
        request.setServletPath("/auth/login");
        request.setRemoteAddr(clientIp);

        MockHttpServletResponse response =
                new MockHttpServletResponse();

        //Act
        filter.doFilter(
                request,
                response,
                new MockFilterChain()
        );

        //Assert
        assertThat(response.getStatus())
                .isEqualTo(HttpStatus.TOO_MANY_REQUESTS.value());

        assertThat(response.getHeader("Retry-After"))
                .isNotBlank();

        assertThat(response.getContentAsString())
                .contains("Límite de intentos excedido");
    }
}
