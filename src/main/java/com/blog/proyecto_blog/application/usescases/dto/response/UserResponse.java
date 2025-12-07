package com.blog.proyecto_blog.application.usescases.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Long idUser;
    private String name;
    private String email;
    private String description;
    private String rol;
}
