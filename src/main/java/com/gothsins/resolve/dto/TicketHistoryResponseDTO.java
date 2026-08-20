package com.gothsins.resolve.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketHistoryResponseDTO {

    private Long id;
    private String action;
    private String oldValue;
    private String newValue;
    private Long ticketId;
    private UserResponseDTO user;
    private LocalDateTime createdAt;
}