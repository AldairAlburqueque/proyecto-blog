package com.blog.proyecto_blog.application.usescases.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BlogSummaryResponse {

    private Long idBlog;
    private String title;
    private String contentPreview;
    private UserSimpleResponse user;
    private CategoryResponse category;
    private LocalDateTime createdAt;
}
