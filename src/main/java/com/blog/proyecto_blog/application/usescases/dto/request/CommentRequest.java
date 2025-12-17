package com.blog.proyecto_blog.application.usescases.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequest {
    @NotBlank(message = "El comentario es obligatorio")
    private String comment;
    @NotNull(message = "El blog es obligatoria")
    private Long blogId;
}
