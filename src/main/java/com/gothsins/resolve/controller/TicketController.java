package com.gothsins.resolve.controller;

import com.gothsins.resolve.dto.TicketRequestDTO;
import com.gothsins.resolve.dto.TicketResponseDTO;
import com.gothsins.resolve.service.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping
    public ResponseEntity<TicketResponseDTO> create(@Valid @RequestBody TicketRequestDTO dto) {
        TicketResponseDTO response = ticketService.create(dto);
        return ResponseEntity.status(201).body(response);
    }
}