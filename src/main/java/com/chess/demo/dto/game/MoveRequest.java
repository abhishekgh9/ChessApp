package com.chess.demo.dto.game;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MoveRequest(
        @NotBlank @Size(min = 2, max = 5) String from,
        @NotBlank @Size(min = 2, max = 5) String to,
        @Size(max = 5) String promotion
) {
}
