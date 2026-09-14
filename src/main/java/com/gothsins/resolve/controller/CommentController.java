package com.gothsins.resolve.controller;

import com.gothsins.resolve.dto.CommentRequestDTO;
import com.gothsins.resolve.dto.CommentResponseDTO;
import com.gothsins.resolve.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
@Tag(name = "Comentários", description = "Endpoints para gerenciamento de comentários em tickets")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    @Operation(
            summary = "Criar um novo comentário",
            description = "Adiciona um comentário a um ticket específico."
    )
    @ApiResponse(responseCode = "201", description = "Comentário criado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    @ApiResponse(responseCode = "404", description = "Usuário ou ticket não encontrado")
    @ApiResponse(responseCode = "429", description = "Comentário duplicado enviado recentemente para este ticket")
    public ResponseEntity<CommentResponseDTO> create(@Valid @RequestBody CommentRequestDTO dto) {
        return ResponseEntity.status(201).body(commentService.create(dto));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar comentário por ID",
            description = "Retorna os detalhes de um comentário específico com base no ID informado."
    )
    @ApiResponse(responseCode = "200", description = "Comentário encontrado com sucesso")
    @ApiResponse(responseCode = "404", description = "Comentário não encontrado")
    public ResponseEntity<CommentResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(commentService.findById(id));
    }

    @GetMapping
    @Operation(
            summary = "Listar todos os comentários",
            description = "Retorna uma lista com todos os comentários associados a tickets."
    )
    @ApiResponse(responseCode = "200", description = "Lista de comentários retornada com sucesso")
    public ResponseEntity<List<CommentResponseDTO>> findAll() {
        return ResponseEntity.ok(commentService.findAll());
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Excluir comentário",
            description = "Exclui um comentário específico com base no ID informado."
    )
    @ApiResponse(responseCode = "204", description = "Comentário excluído com sucesso")
    @ApiResponse(responseCode = "404", description = "Comentário não encontrado")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        commentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}