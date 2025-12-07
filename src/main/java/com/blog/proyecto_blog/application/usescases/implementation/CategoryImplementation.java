package com.blog.proyecto_blog.application.usescases.implementation;

import com.blog.proyecto_blog.application.usescases.dto.request.CategoryRequest;
import com.blog.proyecto_blog.application.usescases.dto.response.CategoryResponse;
import com.blog.proyecto_blog.application.usescases.interfaces.ICategoryInterface;
import com.blog.proyecto_blog.domain.services.interfaces.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryImplementation implements ICategoryInterface {
    private final ICategoryService iCategoryService;

    @Override
    public CategoryResponse createCategory(CategoryRequest request) {
        return iCategoryService.createCategoryService(request);
    }

    @Override
    public CategoryResponse updateCategory(Long id, CategoryRequest request) {
        return iCategoryService.updateCategoryService(id,request);
    }

    @Override
    public CategoryResponse getCategoryById(Long id) {
        return iCategoryService.getCategoryByIdService(id);
    }

    @Override
    public List<CategoryResponse> getAllCategories() {
        return iCategoryService.getAllCategoriesService();
    }

    @Override
    public void deleteCategory(Long id) {
        iCategoryService.deleteCategoryService(id);
    }
}
