package com.blog.proyecto_blog.application.usescases.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserRequest {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar 100 caracteres")
    private String name;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email no tiene un formato válido")
    @Size(max = 254, message = "El email no puede superar 254 caracteres")
    private String email;

    @NotBlank(message = "La descripción es obligatoria")
    @Size(max = 500, message = "La descripción no puede superar 500 caracteres")
    private String description;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(
            min = 12,
            max = 72,
            message = "La contraseña debe tener entre 12 y 72 caracteres"
    )
    private String password;
}
