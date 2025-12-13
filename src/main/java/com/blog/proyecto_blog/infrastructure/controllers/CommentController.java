package com.blog.proyecto_blog.infrastructure.controllers;

import com.blog.proyecto_blog.application.usescases.dto.request.CommentRequest;
import com.blog.proyecto_blog.application.usescases.dto.request.CommentUpdateRequest;
import com.blog.proyecto_blog.application.usescases.dto.response.CommentSimpleResponse;
import com.blog.proyecto_blog.application.usescases.interfaces.ICommentInterface;
import com.blog.proyecto_blog.domain.services.interfaces.ICommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {
    private final ICommentInterface commentInterface;

    // Crear comentario
    @PostMapping("/save")
    public ResponseEntity<CommentSimpleResponse> createComment(
            @RequestBody CommentRequest request
    ) {
        return ResponseEntity.ok(commentInterface.createCommnet(request));
    }

    // Obtener comentarios por blog
//    @GetMapping("/list/{blogId}")
//    public ResponseEntity<List<CommentSimpleResponse>> getCommentsByBlog(
//            @PathVariable Long blogId
//    ) {
//        return ResponseEntity.ok(commentInterface.getCommentsByIdBlog(blogId));
//    }

    // Obtener comentario por ID
//    @GetMapping("/{id}")
//    public ResponseEntity<CommentSimpleResponse> getCommentById(
//            @PathVariable Long id
//    ) {
//        return ResponseEntity.ok(commentInterface.getCommentById(id));
//    }

    // Actualizar comentario
    @PutMapping("update/{id}")
    public ResponseEntity<CommentSimpleResponse> updateComment(
            @PathVariable Long id,
            @RequestBody CommentUpdateRequest request
    ) {
        return ResponseEntity.ok(commentInterface.updateComment(id, request));
    }

    // Eliminar comentario
    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        commentInterface.deleteComment(id);
        return ResponseEntity.noContent().build();
    }
}
