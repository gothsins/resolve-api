package com.gothsins.resolve.controller;

import com.gothsins.resolve.dto.TicketHistoryResponseDTO;
import com.gothsins.resolve.service.TicketHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TicketHistoryController {

    private final TicketHistoryService ticketHistoryService;

    @GetMapping("/api/tickets/{ticketId}/history")
    public List<TicketHistoryResponseDTO> findByTicketId(@PathVariable Long ticketId) {
        return ticketHistoryService.findByTicketId(ticketId);
    }

    @GetMapping("/api/ticket-history/{id}")
    public TicketHistoryResponseDTO findById(@PathVariable Long id) {
        return ticketHistoryService.findById(id);
    }
}