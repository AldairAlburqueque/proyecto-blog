package com.blog.proyecto_blog.domain.services.implementation;

import com.blog.proyecto_blog.application.usescases.dto.request.BlogRequest;
import com.blog.proyecto_blog.application.usescases.dto.response.BlogResponse;
import com.blog.proyecto_blog.application.usescases.mappers.BlogMapper;
import com.blog.proyecto_blog.domain.services.interfaces.IBlogService;
import com.blog.proyecto_blog.infrastructure.database.entity.BlogEntity;
import com.blog.proyecto_blog.infrastructure.database.entity.CategoryEntity;
import com.blog.proyecto_blog.infrastructure.database.entity.UserEntity;
import com.blog.proyecto_blog.infrastructure.database.repositories.BlogRepository;
import com.blog.proyecto_blog.infrastructure.database.repositories.CategoryRepository;
import com.blog.proyecto_blog.infrastructure.database.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
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
    public Page<BlogResponse> getAllBlogsService(Pageable pageable) {
        return blogRepository.findAll(pageable)
                .map(blogMapper::toResponse);
    }

    @Override
    public void deleteBlogService(Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String email = auth.getName();

        BlogEntity blog = blogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog no encontrado"));

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no autenticado"));

        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_Admin"));

        boolean isOwner = blog.getUser().getIdUser().equals(user.getIdUser());

        if (!isOwner && !isAdmin) {
            throw new AccessDeniedException("No tienes permiso para eliminar este blog");
        }

        blogRepository.delete(blog);
    }

    @Override
    public List<BlogResponse> getBlogByCategoryService(Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new RuntimeException("Categoria no encontrada");
        }

        return blogRepository.findByCategory_IdCategory(categoryId)
                .stream()
                .map(blogMapper::toResponse)
                .toList();
    }

    @Override
    public Page<BlogResponse> getBlogByUserService( Pageable pageable) {
        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no autenticado"));

        Page<BlogEntity> blogsPage = blogRepository.findByUser(user, pageable);

        return blogsPage.map(blogMapper::toResponse);
    }

    @Override
    public List<BlogResponse> findByTitleContainingIgnoreCaseService(String title) {
        return blogRepository.findByTitleContainingIgnoreCase(title)
                .stream()
                .map(blogMapper::toResponse)
                .toList();
    }
}
