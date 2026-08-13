package com.gothsins.resolve.controller;

import com.gothsins.resolve.dto.TicketRequestDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    @PostMapping
    public ResponseEntity<TicketRequestDTO> create(@Valid @RequestBody TicketRequestDTO dto) {
        return ResponseEntity.status(201).body(dto);
    }
}