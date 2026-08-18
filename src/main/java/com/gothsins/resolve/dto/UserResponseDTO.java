package com.gothsins.resolve.dto;

import com.gothsins.resolve.entity.enums.Role;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {

    private Long id;
    private String name;
    private String email;
    private Role role;
    private Boolean active;
    private LocalDateTime createdAt;
}