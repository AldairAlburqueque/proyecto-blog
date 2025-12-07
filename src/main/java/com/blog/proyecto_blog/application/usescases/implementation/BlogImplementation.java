package com.blog.proyecto_blog.application.usescases.implementation;

import com.blog.proyecto_blog.application.usescases.dto.request.BlogRequest;
import com.blog.proyecto_blog.application.usescases.dto.request.CommentRequest;
import com.blog.proyecto_blog.application.usescases.dto.response.BlogResponse;
import com.blog.proyecto_blog.application.usescases.dto.response.CommentSimpleResponse;
import com.blog.proyecto_blog.application.usescases.interfaces.IBlogInterface;
import com.blog.proyecto_blog.application.usescases.interfaces.ICommentInterface;
import com.blog.proyecto_blog.domain.services.interfaces.IBlogService;
import lombok.RequiredArgsConstructor;
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
    public List<BlogResponse> getAllBlogs() {
        return iBlogService.getAllBlogsService();
    }

    @Override
    public void deleteBlog(Long id) {
        iBlogService.deleteBlogService(id);
    }

    @Override
    public List<BlogResponse> getBlogByUser(Long userId) {
        return iBlogService.getBlogByUserService(userId);
    }

    @Override
    public List<BlogResponse> getBlogByCategory(Long categoryId) {
        return iBlogService.getBlogByCategoryService(categoryId);
    }
}
