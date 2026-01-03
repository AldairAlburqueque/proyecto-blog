package com.blog.proyecto_blog.domain.services.implementation;

import com.blog.proyecto_blog.application.usescases.dto.request.CategoryRequest;
import com.blog.proyecto_blog.application.usescases.dto.response.CategoryResponse;
import com.blog.proyecto_blog.application.usescases.mappers.CategoryMapper;
import com.blog.proyecto_blog.domain.services.interfaces.ICategoryService;
import com.blog.proyecto_blog.infrastructure.database.entity.CategoryEntity;
import com.blog.proyecto_blog.infrastructure.database.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImplementation implements ICategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryResponse createCategoryService(CategoryRequest request) {
        CategoryEntity entity = categoryMapper.toEntity(request);
        categoryRepository.save(entity);
        return categoryMapper.toResponse(entity);
    }

    @Override
    public CategoryResponse updateCategoryService(Long id, CategoryRequest request) {
        CategoryEntity db = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        db.setCategoria(request.getCategoria());
        db.setDescription(request.getDescription());

        CategoryEntity updated = categoryRepository.save(db);

        return categoryMapper.toResponse(updated);
    }

    @Override
    public CategoryResponse getCategoryByIdService(Long id) {
        CategoryEntity entity = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        return categoryMapper.toResponse(entity);
    }

    @Override
    public List<CategoryResponse> getAllCategoriesService() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toResponse)
                .toList();
    }

    @Override
    public void deleteCategoryService(Long id) {
        categoryRepository.deleteById(id);
    }
}