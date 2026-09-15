package com.gothsins.resolve.controller;

import com.gothsins.resolve.dto.ChangeStatusDTO;
import com.gothsins.resolve.dto.TicketRequestDTO;
import com.gothsins.resolve.dto.TicketResponseDTO;
import com.gothsins.resolve.dto.TicketUpdateDTO;
import com.gothsins.resolve.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
@Tag(name = "Tickets", description = "Endpoints para abertura, atribuição e resolução de chamados ITSM")
public class TicketController {

    private final TicketService ticketService;

    @PostMapping
    @Operation(
            summary = "Criar um novo ticket",
            description = "Registra um incidente ou requisição na plataforma e calcula o SLA baseado na prioridade."
    )
    @ApiResponse(responseCode = "201", description = "Ticket criado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados de requisição inválidos")
    public ResponseEntity<TicketResponseDTO> create(@Valid @RequestBody TicketRequestDTO dto) {
        return ResponseEntity.status(201).body(ticketService.create(dto));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar ticket por ID",
            description = "Retorna os detalhes completos de um ticket, incluindo status de SLA."
    )
    @ApiResponse(responseCode = "200", description = "Ticket encontrado")
    @ApiResponse(responseCode = "404", description = "Ticket não encontrado")
    public ResponseEntity<TicketResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.findById(id));
    }

    @GetMapping
    @Operation(
            summary = "Listar todos os tickets",
            description = "Retorna todos os tickets cadastrados na plataforma."
    )
    @ApiResponse(responseCode = "200", description = "Lista de tickets retornada com sucesso")
    public ResponseEntity<List<TicketResponseDTO>> findAll() {
        return ResponseEntity.ok(ticketService.findAll());
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Atualizar dados gerais do ticket",
            description = "Atualiza título, descrição, prioridade, categoria e agente designado. Não altera o status."
    )
    @ApiResponse(responseCode = "200", description = "Ticket atualizado com sucesso")
    @ApiResponse(responseCode = "404", description = "Ticket não encontrado")
    public ResponseEntity<TicketResponseDTO> update(@PathVariable Long id, @Valid @RequestBody TicketUpdateDTO dto) {
        return ResponseEntity.ok(ticketService.update(id, dto));
    }

    @PatchMapping("/{id}/status")
    @Operation(
            summary = "Alterar status do ticket",
            description = "Muda o status do ticket (ex: OPEN → IN_PROGRESS → RESOLVED), ajustando timestamps de resolução/fechamento e registrando no histórico."
    )
    @ApiResponse(responseCode = "200", description = "Status alterado com sucesso")
    @ApiResponse(responseCode = "404", description = "Ticket ou usuário não encontrado")
    public ResponseEntity<TicketResponseDTO> changeStatus(@PathVariable Long id, @Valid @RequestBody ChangeStatusDTO dto) {
        return ResponseEntity.ok(ticketService.changeStatus(id, dto));
    }
}