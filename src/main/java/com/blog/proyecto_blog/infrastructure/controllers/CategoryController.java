package com.blog.proyecto_blog.infrastructure.controllers;

import com.blog.proyecto_blog.application.usescases.dto.request.CategoryRequest;
import com.blog.proyecto_blog.application.usescases.dto.request.UserRequest;
import com.blog.proyecto_blog.application.usescases.dto.response.CategoryResponse;
import com.blog.proyecto_blog.application.usescases.dto.response.UserResponse;
import com.blog.proyecto_blog.application.usescases.interfaces.ICategoryInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {
    private final ICategoryInterface iCategoryInterface;

    @PostMapping("/save")
    public ResponseEntity<CategoryResponse> createCategory(@RequestBody CategoryRequest request) {
        return ResponseEntity.ok(iCategoryInterface.createCategory(request));
    }

    @PutMapping("update/{id}")
    public ResponseEntity<CategoryResponse> updateUser(
            @PathVariable String id,
            @RequestBody CategoryRequest request
    ) {
        Long idUser = Long.parseLong(id);
        return ResponseEntity.ok(iCategoryInterface.updateCategory(idUser, request));
    }

    @GetMapping("/list")
    public ResponseEntity<List<CategoryResponse>> getAllCategory() {
        return ResponseEntity.ok(iCategoryInterface.getAllCategories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getByIdCategory(@PathVariable Long id) {
        return ResponseEntity.ok(iCategoryInterface.getCategoryById(id));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        iCategoryInterface.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
