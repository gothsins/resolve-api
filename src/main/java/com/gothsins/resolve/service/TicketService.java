package com.gothsins.resolve.service;

import com.gothsins.resolve.dto.*;
import com.gothsins.resolve.entity.Category;
import com.gothsins.resolve.entity.Ticket;
import com.gothsins.resolve.entity.User;
import com.gothsins.resolve.entity.enums.TicketStatus;
import com.gothsins.resolve.exception.ResourceNotFoundException;
import com.gothsins.resolve.repository.CategoryRepository;
import com.gothsins.resolve.repository.TicketRepository;
import com.gothsins.resolve.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final TicketHistoryService ticketHistoryService;
    private final MetricsService metricsService;

    @Transactional
    public TicketResponseDTO create(TicketRequestDTO dto) {
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Categoria não encontrada: id " + dto.getCategoryId()));

        User requester = userRepository.findById(dto.getRequesterId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuário (requester) não encontrado: id " + dto.getRequesterId()));

        User assignedAgent = null;
        if (dto.getAssignedAgentId() != null) {
            assignedAgent = userRepository.findById(dto.getAssignedAgentId())
                    .orElseThrow(() -> new ResourceNotFoundException(
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
        metricsService.incrementTicketCreated(saved.getPriority());

        return toResponseDTO(saved);
    }

    @Transactional
    public TicketResponseDTO update(Long id, TicketUpdateDTO dto) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Ticket não encontrado: id " + id));

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Categoria não encontrada: id " + dto.getCategoryId()));

        User assignedAgent = null;
        if (dto.getAssignedAgentId() != null) {
            assignedAgent = userRepository.findById(dto.getAssignedAgentId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Agente não encontrado: id " + dto.getAssignedAgentId()));
        }

        ticket.setTitle(dto.getTitle());
        ticket.setDescription(dto.getDescription());
        ticket.setPriority(dto.getPriority());
        ticket.setCategory(category);
        ticket.setAssignedAgent(assignedAgent);

        Ticket updated = ticketRepository.save(ticket);
        return toResponseDTO(updated);
    }

    @Transactional
    public TicketResponseDTO changeStatus(Long id, ChangeStatusDTO dto) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Ticket não encontrado: id " + id));

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuário não encontrado: id " + dto.getUserId()));

        TicketStatus oldStatus = ticket.getStatus();
        TicketStatus newStatus = dto.getNewStatus();

        ticket.setStatus(newStatus);

        if (newStatus == TicketStatus.RESOLVED) {
            ticket.setResolvedAt(LocalDateTime.now());
        }
        if (newStatus == TicketStatus.CLOSED) {
            ticket.setClosedAt(LocalDateTime.now());
        }

        Ticket updated = ticketRepository.save(ticket);

        ticketHistoryService.registerChange(
                updated, user, "STATUS_CHANGE", oldStatus.name(), newStatus.name());
        metricsService.incrementTicketStatusChanged(oldStatus.name(), newStatus.name());

        return toResponseDTO(updated);
    }

    @Transactional(readOnly = true)
    public TicketResponseDTO findById(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Ticket não encontrado: id " + id));
        return toResponseDTO(ticket);
    }

    @Transactional(readOnly = true)
    public List<TicketResponseDTO> findAll() {
        return ticketRepository.findAll().stream()
                .map(this::toResponseDTO)
                .toList();
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
                .active(category.getActive())
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