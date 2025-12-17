package com.blog.proyecto_blog.application.usescases.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlogRequest {
    @NotBlank(message = "El título es obligatorio")
    private String title;
    @NotBlank(message = "El contenido es obligatorio")
    private String content;
    @NotNull(message = "La categoría es obligatoria")
    private Long categoryId;
}
