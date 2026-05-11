package com.blog.proyecto_blog.application.usescases.implementation;

import com.blog.proyecto_blog.application.usescases.dto.request.BlogRequest;
import com.blog.proyecto_blog.application.usescases.dto.response.BlogResponse;
import com.blog.proyecto_blog.application.usescases.interfaces.IBlogInterface;
import com.blog.proyecto_blog.domain.services.interfaces.IBlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BlogImplementation implements IBlogInterface {
    private final IBlogService iBlogService;

    @Override
    public BlogResponse createBlog(BlogRequest request) {
        return iBlogService.createBlogService(request);
    }

    @Override
    public BlogResponse updateBlog(Long id, BlogRequest request) {
        return iBlogService.updateBlogService(id,request);
    }

    @Override
    public BlogResponse getBlogById(Long id) {
        return iBlogService.getBlogByIdService(id);
    }

    @Override
    public Page<BlogResponse> getAllBlogs(Pageable pageable) {
        return iBlogService.getAllBlogsService(pageable);
    }

    @Override
    public void deleteBlog(Long id) {
        iBlogService.deleteBlogService(id);
    }

    @Override
    public Page<BlogResponse> getBlogByUser(Pageable pageable) {
        return iBlogService.getBlogByUserService(pageable);
    }

    @Override
    public Page<BlogResponse> getBlogByCategory(Long categoryId, Pageable pageable) {
        return iBlogService.getBlogByCategoryService(categoryId, pageable);
    }

    @Override
    public Page<BlogResponse> findByTitleContainingIgnoreCase(String title, Pageable pageable) {
        return iBlogService.findByTitleContainingIgnoreCaseService(title, pageable);
    }
}
