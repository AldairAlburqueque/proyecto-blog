package com.blog.proyecto_blog.infrastructure.controllers;

import com.blog.proyecto_blog.application.usescases.dto.request.CommentRequest;
import com.blog.proyecto_blog.application.usescases.dto.request.CommentUpdateRequest;
import com.blog.proyecto_blog.application.usescases.dto.response.CommentResponse;
import com.blog.proyecto_blog.application.usescases.dto.response.CommentSimpleResponse;
import com.blog.proyecto_blog.application.usescases.interfaces.ICommentInterface;
import com.blog.proyecto_blog.domain.services.interfaces.ICommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {
    private final ICommentInterface commentInterface;


    @PostMapping("/save")
    public ResponseEntity<CommentSimpleResponse> createComment(
            @Valid
            @RequestBody CommentRequest request
    ) {
        CommentSimpleResponse response = commentInterface.createCommnet(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<CommentSimpleResponse> updateComment(
            @PathVariable Long id,
            @Valid @RequestBody CommentUpdateRequest request
    ) {
        return ResponseEntity.ok(commentInterface.updateComment(id, request));
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        commentInterface.deleteComment(id);
        return ResponseEntity.noContent().build();
    }
}
