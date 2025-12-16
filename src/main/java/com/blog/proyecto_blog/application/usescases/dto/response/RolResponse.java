package com.blog.proyecto_blog.application.usescases.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RolResponse {
    private Long idRol;
    private String rol;
}
