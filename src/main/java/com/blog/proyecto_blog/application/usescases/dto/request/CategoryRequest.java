package com.blog.proyecto_blog.application.usescases.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRequest {
    @NotBlank(message = "La categoria es obligatoria")
    private String categoria;
    @NotBlank(message = "Debe de escribir una descripción")
    private String description;
}
