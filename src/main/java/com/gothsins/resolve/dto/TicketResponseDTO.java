package com.gothsins.resolve.dto;
import com.gothsins.resolve.entity.enums.TicketPriority;
import com.gothsins.resolve.entity.enums.TicketStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketResponseDTO {

    private Long id;
    private String title;
    private String description;
    private TicketStatus status;
    private TicketPriority priority;

    private CategoryResponseDTO category;
    private UserResponseDTO requester;
    private UserResponseDTO assignedAgent;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime resolvedAt;
    private LocalDateTime closedAt;
}
