package com.chess.demo.dto.game;

public record CreateGameRequest(
        String timeControl,
        Boolean rated,
        String colorPreference
) {
}
