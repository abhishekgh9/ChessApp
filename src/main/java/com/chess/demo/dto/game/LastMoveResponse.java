package com.chess.demo.dto.game;

public record LastMoveResponse(
        String from,
        String to,
        String san
) {
}
