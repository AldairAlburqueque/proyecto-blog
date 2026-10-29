package com.blog.proyecto_blog.domain.services.implementation;

import com.blog.proyecto_blog.application.usescases.dto.request.BlogRequest;
import com.blog.proyecto_blog.application.usescases.dto.response.BlogResponse;
import com.blog.proyecto_blog.application.usescases.dto.response.BlogSummaryResponse;
import com.blog.proyecto_blog.application.usescases.mappers.BlogMapper;
import com.blog.proyecto_blog.domain.exceptions.InvalidContentException;
import com.blog.proyecto_blog.domain.exceptions.InvalidCredentialsException;
import com.blog.proyecto_blog.domain.exceptions.ResourceNotFoundException;
import com.blog.proyecto_blog.domain.services.interfaces.IBlogService;
import com.blog.proyecto_blog.infrastructure.UserContentSanitizer;
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
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class BlogServiceImplementation implements IBlogService {
    private final BlogRepository blogRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final BlogMapper blogMapper;
    private final UserContentSanitizer userContentSanitizer;

    @Override
    public BlogResponse createBlogService(BlogRequest request) {
        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        UserEntity author = userRepository.findByEmail(email)
                .orElseThrow(InvalidCredentialsException::new);

        CategoryEntity category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Categoría",
                        request.getCategoryId()
                ));

        BlogEntity entity = blogMapper.toEntity(request, author, category);
        entity.setTitle(sanitizePlainText(request.getTitle()));
        entity.setContent(sanitizeMarkdown(request.getContent()));

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
                .orElseThrow(() -> new ResourceNotFoundException("Blog", id));

        UserEntity author = userRepository.findByEmail(email)
                .orElseThrow(InvalidCredentialsException::new);

        if (!blog.getUser().getIdUser().equals(author.getIdUser())){
            throw new AccessDeniedException("No tienes permiso para editar este blog");
        }

        CategoryEntity category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Categoría",
                        request.getCategoryId()
                ));

        blog.setTitle(sanitizePlainText(request.getTitle()));
        blog.setContent(sanitizeMarkdown(request.getContent()));
        blog.setCategory(category);

        BlogEntity updated = blogRepository.save(blog);

        return blogMapper.toResponse(updated);
    }

    @Override
    public BlogResponse getBlogByIdService(Long id) {
        BlogEntity blog = blogRepository.findByIdBlog(id)
                .orElseThrow(() -> new ResourceNotFoundException("Blog", id));

        return blogMapper.toResponse(blog);
    }

    @Override
    public Page<BlogSummaryResponse> getAllBlogsService(Pageable pageable) {
        return blogRepository.findAll(pageable)
                .map(blogMapper::toSummaryResponse);
    }

    @Override
    public void deleteBlogService(Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String email = auth.getName();

        BlogEntity blog = blogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Blog", id));

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(InvalidCredentialsException::new);

        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_Admin"));

        boolean isOwner = blog.getUser().getIdUser().equals(user.getIdUser());

        if (!isOwner && !isAdmin) {
            throw new AccessDeniedException("No tienes permiso para eliminar este blog");
        }

        blogRepository.delete(blog);
    }

    @Override
    public Page<BlogSummaryResponse> getBlogByCategoryService(Long categoryId, Pageable pageable) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new ResourceNotFoundException("Categoría", categoryId);
        }

        return blogRepository.findByCategory_IdCategory(categoryId, pageable)
                .map(blogMapper::toSummaryResponse);
    }

    @Override
    public Page<BlogSummaryResponse> getBlogByUserService( Pageable pageable) {
        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no autenticado"));

        Page<BlogEntity> blogsPage = blogRepository.findByUser(user, pageable);

        return blogsPage.map(blogMapper::toSummaryResponse);
    }

    @Override
    public Page<BlogSummaryResponse> findByTitleContainingIgnoreCaseService(String title, Pageable pageable) {
        return blogRepository.findByTitleContainingIgnoreCase(title, pageable)
                //.stream()
                .map(blogMapper::toSummaryResponse);
                //.toList();
    }

    private String sanitizeMarkdown(String content) {
        String sanitized = userContentSanitizer.sanitizerMarkdown(content);

        if (!StringUtils.hasText(sanitized)) {
            throw new InvalidContentException();
        }

        return sanitized;
    }

    private String sanitizePlainText(String content) {
        String sanitized = userContentSanitizer.sanitizerPlainText(content);

        if (!StringUtils.hasText(sanitized)) {
            throw new InvalidContentException();
        }

        return sanitized;
    }
}
