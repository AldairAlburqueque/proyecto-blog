package com.blog.proyecto_blog.domain.services.interfaces;

import com.blog.proyecto_blog.application.usescases.dto.request.BlogRequest;
import com.blog.proyecto_blog.application.usescases.dto.response.BlogResponse;
import com.blog.proyecto_blog.infrastructure.database.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.List;

public interface IBlogService {
    BlogResponse createBlogService(BlogRequest request);
    BlogResponse updateBlogService(Long id, BlogRequest request);
    BlogResponse getBlogByIdService(Long id);
    //List<BlogResponse> getAllBlogsService();
    Page<BlogResponse> getAllBlogsService(Pageable pageable);
    void deleteBlogService(Long id);
    //List<BlogResponse> getBlogByUserService(Long userId);
    List<BlogResponse> getBlogByCategoryService(Long categoryId);
    //Probando traer los blogs del usuario logueado
    Page<BlogResponse> getBlogByUserService( Pageable pageable);
}
