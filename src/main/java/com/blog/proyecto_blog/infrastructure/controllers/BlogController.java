package com.blog.proyecto_blog.infrastructure.controllers;

import com.blog.proyecto_blog.application.usescases.dto.request.BlogRequest;
import com.blog.proyecto_blog.application.usescases.dto.response.BlogResponse;
import com.blog.proyecto_blog.application.usescases.interfaces.IBlogInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/blog")
@RequiredArgsConstructor
public class BlogController {
    private final IBlogInterface iBlogInterface;

    // CREATE
    @PostMapping("/create")
    public ResponseEntity<BlogResponse> createBlog(@RequestBody BlogRequest request) {
        return ResponseEntity.ok(iBlogInterface.createBlog(request));
    }

    // UPDATE
    @PutMapping("/update/{id}")
    public ResponseEntity<BlogResponse> updateBlog(
            @PathVariable Long id,
            @RequestBody BlogRequest request
    ) {
        return ResponseEntity.ok(iBlogInterface.updateBlog(id, request));
    }

    // DELETE
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteBlog(@PathVariable Long id) {
        iBlogInterface.deleteBlog(id);
        return ResponseEntity.noContent().build();
    }


    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<BlogResponse> getBlogById(@PathVariable Long id) {
        return ResponseEntity.ok(iBlogInterface.getBlogById(id));
    }

    // LIST ALL
//    @GetMapping("/list")
//    public ResponseEntity<List<BlogResponse>> getAllBlogs() {
//        return ResponseEntity.ok(iBlogInterface.getAllBlogs());
//    }
    @GetMapping("/list")
    public ResponseEntity<Page<BlogResponse>> getAllBlogs(
            @PageableDefault(size = 5, sort = "createdAt")Pageable pageable
            ) {
        return ResponseEntity.ok(iBlogInterface.getAllBlogs(pageable));
    }

    // LISTAR POR USER
    @GetMapping("/me")
    public ResponseEntity<Page<BlogResponse>> getBlogsByUser(
            @PageableDefault(size = 5, sort = "createdAt")Pageable pageable
    ) {
        return ResponseEntity.ok(iBlogInterface.getBlogByUser(pageable));
    }

    // LISTAR POR CATEGORY
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<BlogResponse>> getBlogsByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(iBlogInterface.getBlogByCategory(categoryId));
    }

    //BUSCAR POR TITLE
    @GetMapping("/search")
    public ResponseEntity<List<BlogResponse>> searchBlogs(
            @RequestParam String title
    ) {
        return ResponseEntity.ok(iBlogInterface.findByTitleContainingIgnoreCase(title));
    }

}
