package com.gothsins.resolve.dto;

import com.gothsins.resolve.entity.enums.TicketPriority;
import com.gothsins.resolve.entity.enums.TicketStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketRequestDTO {
    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @NotNull
    private TicketPriority priority;

    private TicketStatus status;

    @NotNull
    private Long categoryId;

    @NotNull
    private Long requesterId;

    private Long assignedAgentId;
}