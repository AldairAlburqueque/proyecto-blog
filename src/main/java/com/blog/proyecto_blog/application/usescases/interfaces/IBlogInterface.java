package com.blog.proyecto_blog.application.usescases.interfaces;

import com.blog.proyecto_blog.application.usescases.dto.request.BlogRequest;
import com.blog.proyecto_blog.application.usescases.dto.response.BlogResponse;
import com.blog.proyecto_blog.application.usescases.dto.response.BlogSummaryResponse;
import com.blog.proyecto_blog.application.usescases.dto.response.CommentSimpleResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface  IBlogInterface {
    BlogResponse createBlog(BlogRequest request);
    BlogResponse updateBlog(Long id, BlogRequest request);
    BlogResponse getBlogById(Long id);
    Page<BlogSummaryResponse> getAllBlogs(Pageable pageable);
    void deleteBlog(Long id);
    Page<BlogSummaryResponse> getBlogByUser(Pageable pageable);
    //List<BlogResponse> getBlogByCategory(Long categoryId);
    Page<BlogSummaryResponse> getBlogByCategory(Long categoryId, Pageable pageable);
    //List<BlogResponse> findByTitleContainingIgnoreCase(String title);
    Page<BlogSummaryResponse> findByTitleContainingIgnoreCase(String title, Pageable pageable);
}
