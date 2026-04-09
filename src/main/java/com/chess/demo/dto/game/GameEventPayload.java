package com.chess.demo.dto.game;

public record GameEventPayload(
        String type,
        GameResponse game,
        Object payload
) {
}
