package com.blog.proyecto_blog.application.usescases.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentUpdateRequest {
    @NotBlank(message = "El comentario es obligatorio")
    @Size(
            max = 2000,
            message = "El comentario no puede superar 2000 caracteres"
    )
    private String comment;
}
