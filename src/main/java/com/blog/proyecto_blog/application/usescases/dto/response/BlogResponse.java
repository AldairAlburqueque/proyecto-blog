package com.blog.proyecto_blog.application.usescases.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BlogResponse {
    private Long idBlog;
    private String title;
    private String content;
    private String state;
    private UserSimpleResponse user;
    private CategoryResponse category;
    private List<CommentSimpleResponse> comments;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
