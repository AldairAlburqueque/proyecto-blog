package com.blog.proyecto_blog.application.usescases.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlogRequest {
    private String title;
    private String content;
    private Long userId;
    private Long categoryId;
}
