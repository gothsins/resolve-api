package com.gothsins.resolve.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangePasswordDTO {

    @NotBlank
    private String currentPassword;

    @NotBlank
    private String newPassword;
}