package com.blog.proyecto_blog.application.usescases.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRequest {
    @NotBlank(message = "La categoria es obligatoria")
    @Size(max = 100, message = "La categoría no puede superar 100 caracteres")
    private String categoria;
    @NotBlank(message = "Debe de escribir una descripción")
    @Size(max = 500, message = "La descripción no puede superar 500 caracteres")
    private String description;
}
