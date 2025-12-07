package com.blog.proyecto_blog.application.usescases.interfaces;

import com.blog.proyecto_blog.application.usescases.dto.request.BlogRequest;
import com.blog.proyecto_blog.application.usescases.dto.response.BlogResponse;

import java.util.List;

public interface  IBlogInterface {
    BlogResponse createBlog(BlogRequest request);
    BlogResponse updateBlog(Long id, BlogRequest request);
    BlogResponse getBlogById(Long id);
    List<BlogResponse> getAllBlogs();
    void deleteBlog(Long id);
    List<BlogResponse> getBlogByUser(Long userId);
    List<BlogResponse> getBlogByCategory(Long categoryId);
}
