package com.blog.proyecto_blog.application.usescases.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    @NotBlank(message = "El nombre es obligatorio")
    private String name;
    @NotBlank(message = "El email es obligatorio")
    private String email;
    @NotBlank(message = "La descripcion es obligatorio")
    private String description;
    @NotBlank(message = "El password es obligatorio")
    private String password;
    @NotNull(message = "El rol es obligatoria")
    private Long rolId;
}
