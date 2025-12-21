package com.blog.proyecto_blog.application.usescases.interfaces;

import com.blog.proyecto_blog.application.usescases.dto.request.BlogRequest;
import com.blog.proyecto_blog.application.usescases.dto.response.BlogResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.List;

public interface  IBlogInterface {
    BlogResponse createBlog(BlogRequest request);
    BlogResponse updateBlog(Long id, BlogRequest request);
    BlogResponse getBlogById(Long id);
    Page<BlogResponse> getAllBlogs(Pageable pageable);
    void deleteBlog(Long id);
    Page<BlogResponse> getBlogByUser(Pageable pageable);
    List<BlogResponse> getBlogByCategory(Long categoryId);
    List<BlogResponse> findByTitleContainingIgnoreCase(String title);
}
