package com.blog.proyecto_blog.application.usescases.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequest {
    private String comment;
    private String state;
    private Long userId;
    private Long blogId;
}
