package com.blog.proyecto_blog.domain.services.interfaces;

import com.blog.proyecto_blog.application.usescases.dto.request.CategoryRequest;
import com.blog.proyecto_blog.application.usescases.dto.response.CategoryResponse;

import java.util.List;

public interface ICategoryService {
    CategoryResponse createCategoryService(CategoryRequest request);
    CategoryResponse updateCategoryService(Long id, CategoryRequest request);
    CategoryResponse getCategoryByIdService(Long id);
    List<CategoryResponse> getAllCategoriesService();
    void deleteCategoryService(Long id);
}
