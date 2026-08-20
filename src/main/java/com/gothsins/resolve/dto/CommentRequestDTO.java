package com.gothsins.resolve.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentRequestDTO {

    @NotBlank
    private String content;

    @NotNull
    private Long ticketId;

    @NotNull
    private Long authorId;
}