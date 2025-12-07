package com.blog.proyecto_blog.application.usescases.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponse {
    private Long idComment;
    private String comment;
    private UserSimpleResponse user;
    private Long blogId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
