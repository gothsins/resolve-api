package com.gothsins.resolve.dto;

import com.gothsins.resolve.entity.enums.TicketStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangeStatusDTO {

    @NotNull
    private TicketStatus newStatus;

    @NotNull
    private Long userId;
}