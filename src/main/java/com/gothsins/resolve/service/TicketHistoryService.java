package com.gothsins.resolve.service;

import com.gothsins.resolve.dto.TicketHistoryResponseDTO;
import com.gothsins.resolve.dto.UserResponseDTO;
import com.gothsins.resolve.entity.Ticket;
import com.gothsins.resolve.entity.TicketHistory;
import com.gothsins.resolve.entity.User;
import com.gothsins.resolve.exception.ResourceNotFoundException;
import com.gothsins.resolve.repository.TicketHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketHistoryService {

    private final TicketHistoryRepository ticketHistoryRepository;

    @Transactional
    public void registerChange(Ticket ticket, User user, String action, String oldValue, String newValue) {
        TicketHistory history = TicketHistory.builder()
                .ticket(ticket)
                .user(user)
                .action(action)
                .oldValue(oldValue)
                .newValue(newValue)
                .build();

        ticketHistoryRepository.save(history);
    }

    @Transactional(readOnly = true)
    public TicketHistoryResponseDTO findById(Long id) {
        TicketHistory history = ticketHistoryRepository.findByWithTicket(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Histórico não encontrado: id " + id));
        return toResponseDTO(history);
    }

    @Transactional(readOnly = true)
    public List<TicketHistoryResponseDTO> findByTicketId(Long ticketId) {
        return ticketHistoryRepository.findByTicketIdOrderByCreatedAtDesc(ticketId).stream()
                .map(this::toResponseDTO)
                .toList();
    }

    private TicketHistoryResponseDTO toResponseDTO(TicketHistory history) {
        return TicketHistoryResponseDTO.builder()
                .id(history.getId())
                .action(history.getAction())
                .oldValue(history.getOldValue())
                .newValue(history.getNewValue())
                .ticketId(history.getTicket().getId())
                .user(toUserDTO(history.getUser()))
                .createdAt(history.getCreatedAt())
                .build();
    }

    private UserResponseDTO toUserDTO(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .active(user.getActive())
                .createdAt(user.getCreatedAt())
                .build();
    }
}