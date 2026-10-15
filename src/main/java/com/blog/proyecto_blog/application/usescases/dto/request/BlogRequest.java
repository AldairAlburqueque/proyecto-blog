package com.blog.proyecto_blog.application.usescases.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlogRequest {
    @NotBlank(message = "El título es obligatorio")
    @Size(max = 200, message = "El título no puede superar 200 caracteres")
    private String title;
    @NotBlank(message = "El contenido es obligatorio")
    @Size(
            max = 10000,
            message = "El contenido no puede superar 10000 caracteres"
    )
    private String content;
    @NotNull(message = "La categoría es obligatoria")
    private Long categoryId;
}
