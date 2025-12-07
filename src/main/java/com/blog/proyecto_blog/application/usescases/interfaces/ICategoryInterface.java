package com.blog.proyecto_blog.application.usescases.interfaces;

import com.blog.proyecto_blog.application.usescases.dto.request.CategoryRequest;
import com.blog.proyecto_blog.application.usescases.dto.response.CategoryResponse;

import java.util.List;

public interface ICategoryInterface {
    CategoryResponse createCategory(CategoryRequest request);
    CategoryResponse updateCategory(Long id, CategoryRequest request);
    CategoryResponse getCategoryById(Long id);
    List<CategoryResponse> getAllCategories();
    void deleteCategory(Long id);
}
