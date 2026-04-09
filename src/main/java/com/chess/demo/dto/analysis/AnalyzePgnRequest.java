package com.chess.demo.dto.analysis;

import jakarta.validation.constraints.NotBlank;

public record AnalyzePgnRequest(@NotBlank String pgn) {
}
