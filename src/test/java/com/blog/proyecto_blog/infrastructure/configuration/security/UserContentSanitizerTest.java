package com.blog.proyecto_blog.infrastructure.configuration.security;

import com.blog.proyecto_blog.infrastructure.UserContentSanitizer;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class UserContentSanitizerTest {

    private final UserContentSanitizer sanitizer = new UserContentSanitizer();

    @Test
    void shouldKeepValidPlainText() {
        //Arrange
        String content = "Comentario válido";

        //Act
        String sanitized = sanitizer.sanitizerPlainText(content);

        //Assert
        assertThat(sanitized).isEqualTo("Comentario válido");
    }

    @Test
    void shouldRemoveHtmlAndKeepSafeText() {
        //Arrange
        String content = "<script>alert('xss')</script>Comentario válido";

        //Act
        String sanitized = sanitizer.sanitizerPlainText(content);

        //Assert
        assertThat(sanitized).isEqualTo("Comentario válido");
    }

    @Test
    void shouldReturnBlankWhenContentContainsOnlyHtml() {
        //Arrange
        String content = "<img src=x onerror=alert(1)>";

        //Act
        String sanitized = sanitizer.sanitizerPlainText(content);

        //Assert
        assertThat(sanitized).isBlank();
    }
}
