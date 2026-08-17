package com.gothsins.resolve.service;

import com.gothsins.resolve.dto.TicketRequestDTO;
import com.gothsins.resolve.dto.TicketResponseDTO;
import com.gothsins.resolve.dto.CategoryResponseDTO;
import com.gothsins.resolve.dto.UserResponseDTO;
import com.gothsins.resolve.entity.Category;
import com.gothsins.resolve.entity.Ticket;
import com.gothsins.resolve.entity.User;
import com.gothsins.resolve.repository.CategoryRepository;
import com.gothsins.resolve.repository.TicketRepository;
import com.gothsins.resolve.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public TicketResponseDTO create(TicketRequestDTO dto) {
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Categoria não encontrada: id " + dto.getCategoryId()));

        User requester = userRepository.findById(dto.getRequesterId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Usuário (requester) não encontrado: id " + dto.getRequesterId()));

        User assignedAgent = null;
        if (dto.getAssignedAgentId() != null) {
            assignedAgent = userRepository.findById(dto.getAssignedAgentId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Agente não encontrado: id " + dto.getAssignedAgentId()));
        }

        Ticket ticket = Ticket.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .priority(dto.getPriority())
                .category(category)
                .requester(requester)
                .assignedAgent(assignedAgent)
                .build();

        Ticket saved = ticketRepository.save(ticket);

        return toResponseDTO(saved);
    }

    private TicketResponseDTO toResponseDTO(Ticket ticket) {
        return TicketResponseDTO.builder()
                .id(ticket.getId())
                .title(ticket.getTitle())
                .description(ticket.getDescription())
                .status(ticket.getStatus())
                .priority(ticket.getPriority())
                .category(toCategoryDTO(ticket.getCategory()))
                .requester(toUserDTO(ticket.getRequester()))
                .assignedAgent(ticket.getAssignedAgent() != null ? toUserDTO(ticket.getAssignedAgent()) : null)
                .createdAt(ticket.getCreatedAt())
                .updatedAt(ticket.getUpdatedAt())
                .resolvedAt(ticket.getResolvedAt())
                .closedAt(ticket.getClosedAt())
                .build();
    }

    private CategoryResponseDTO toCategoryDTO(Category category) {
        return CategoryResponseDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    private UserResponseDTO toUserDTO(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }
}