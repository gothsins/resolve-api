package com.gothsins.resolve.controller;

import com.gothsins.resolve.dto.CommentRequestDTO;
import com.gothsins.resolve.dto.CommentResponseDTO;
import com.gothsins.resolve.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentResponseDTO> create(@Valid @RequestBody CommentRequestDTO dto) {
        return ResponseEntity.status(201).body(commentService.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(commentService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<CommentResponseDTO>> findAll() {
        return ResponseEntity.ok(commentService.findAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        commentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}