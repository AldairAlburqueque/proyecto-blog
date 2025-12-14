package com.blog.proyecto_blog.domain.services.implementation;

import com.blog.proyecto_blog.application.usescases.dto.request.BlogRequest;
import com.blog.proyecto_blog.application.usescases.dto.response.BlogResponse;
import com.blog.proyecto_blog.application.usescases.interfaces.IBlogInterface;
import com.blog.proyecto_blog.application.usescases.mappers.BlogMapper;
import com.blog.proyecto_blog.domain.services.interfaces.IBlogService;
import com.blog.proyecto_blog.infrastructure.database.entity.BlogEntity;
import com.blog.proyecto_blog.infrastructure.database.entity.CategoryEntity;
import com.blog.proyecto_blog.infrastructure.database.entity.UserEntity;
import com.blog.proyecto_blog.infrastructure.database.repositories.BlogRepository;
import com.blog.proyecto_blog.infrastructure.database.repositories.CategoryRepository;
import com.blog.proyecto_blog.infrastructure.database.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BlogServiceImplementation implements IBlogService {
    private final BlogRepository blogRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final BlogMapper blogMapper;


    @Override
    public BlogResponse createBlogService(BlogRequest request) {
        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        UserEntity author = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no autenticado"));

        CategoryEntity category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        BlogEntity entity = blogMapper.toEntity(request, author, category);

        BlogEntity saved = blogRepository.save(entity);

        return blogMapper.toResponse(saved);
    }

    @Override
    public BlogResponse updateBlogService(Long id, BlogRequest request) {
        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        BlogEntity blog = blogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog no encontrado"));

        UserEntity author = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no autenticado"));

        if (!blog.getUser().getIdUser().equals(author.getIdUser())){
            throw new RuntimeException("No tienes permiso para editar este blog");
        }

        CategoryEntity category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        // Actualizar valores
        blog.setTitle(request.getTitle());
        blog.setContent(request.getContent());
        blog.setCategory(category);

        BlogEntity updated = blogRepository.save(blog);

        return blogMapper.toResponse(updated);
    }

    @Override
    public BlogResponse getBlogByIdService(Long id) {
        BlogEntity blog = blogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog no encontrado"));

        return blogMapper.toResponse(blog);
    }

    @Override
    public List<BlogResponse> getAllBlogsService() {
        return blogRepository.findAll()
                .stream()
                .map(blogMapper::toResponse)
                .toList();
    }


    @Override
    public void deleteBlogService(Long id) {
        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        BlogEntity blog = blogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog no encontrado"));

        UserEntity author = userRepository.findByEmail(email)
                        .orElseThrow(() -> new RuntimeException("Usuario no autenticado"));

        if (!blog.getUser().getIdUser().equals(author.getIdUser())) {
            throw new RuntimeException("No tienes permiso para eliminar este blog");
        }

        blogRepository.delete(blog);
    }

    @Override
    public List<BlogResponse> getBlogByUserService(Long userId) {
        return blogRepository.findByUserIdUser(userId)
                .stream()
                .map(blogMapper::toResponse)
                .toList();
    }

    @Override
    public List<BlogResponse> getBlogByCategoryService(Long categoryId) {
        return blogRepository.findByCategory_IdCategory(categoryId)
                .stream()
                .map(blogMapper::toResponse)
                .toList();
    }
}
