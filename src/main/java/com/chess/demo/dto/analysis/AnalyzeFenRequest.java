package com.chess.demo.dto.analysis;

import jakarta.validation.constraints.NotBlank;

public record AnalyzeFenRequest(@NotBlank String fen) {
}
