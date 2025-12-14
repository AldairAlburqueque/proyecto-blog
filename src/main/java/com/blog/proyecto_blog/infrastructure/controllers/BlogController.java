package com.blog.proyecto_blog.infrastructure.controllers;

import com.blog.proyecto_blog.application.usescases.dto.request.BlogRequest;
import com.blog.proyecto_blog.application.usescases.dto.response.BlogResponse;
import com.blog.proyecto_blog.application.usescases.interfaces.IBlogInterface;
import lombok.RequiredArgsConstructor;
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
    @GetMapping("/list")
    public ResponseEntity<List<BlogResponse>> getAllBlogs() {
        return ResponseEntity.ok(iBlogInterface.getAllBlogs());
    }

    // LIST BY USER
    @GetMapping("/list/{userId}")
    public ResponseEntity<List<BlogResponse>> getBlogsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(iBlogInterface.getBlogByUser(userId));
    }

    // LIST BY CATEGORY
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<BlogResponse>> getBlogsByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(iBlogInterface.getBlogByCategory(categoryId));
    }

}
