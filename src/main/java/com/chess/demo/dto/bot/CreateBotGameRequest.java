package com.chess.demo.dto.bot;

public record CreateBotGameRequest(
        Integer level,
        String color
) {
}
