package com.blog.proyecto_blog.domain.services.interfaces;

import com.blog.proyecto_blog.application.usescases.dto.request.BlogRequest;
import com.blog.proyecto_blog.application.usescases.dto.response.BlogResponse;
import com.blog.proyecto_blog.application.usescases.dto.response.BlogSummaryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IBlogService {
    BlogResponse createBlogService(BlogRequest request);
    BlogResponse updateBlogService(Long id, BlogRequest request);
    BlogResponse getBlogByIdService(Long id);
    //List<BlogResponse> getAllBlogsService();
    Page<BlogSummaryResponse> getAllBlogsService(Pageable pageable);
    void deleteBlogService(Long id);
    //List<BlogResponse> getBlogByCategoryService(Long categoryId);
    Page<BlogSummaryResponse> getBlogByCategoryService(Long categoryId, Pageable pageable);
    //Probando traer los blogs del usuario logueado
    Page<BlogSummaryResponse> getBlogByUserService( Pageable pageable);
    //List<BlogResponse> findByTitleContainingIgnoreCaseService(String title);
    Page<BlogSummaryResponse> findByTitleContainingIgnoreCaseService(String title, Pageable pageable);
}
