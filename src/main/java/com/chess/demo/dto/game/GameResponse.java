package com.chess.demo.dto.game;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record GameResponse(
        UUID gameId,
        UUID whitePlayerId,
        UUID blackPlayerId,
        String fen,
        String pgn,
        List<String> history,
        String timeControl,
        Integer whiteTimeRemaining,
        Integer blackTimeRemaining,
        String status,
        String result,
        String resultReason,
        LastMoveResponse lastMove,
        Boolean rated,
        Boolean isBotGame,
        Integer botLevel,
        String turnColor,
        UUID drawOfferedBy,
        Instant createdAt,
        Instant updatedAt
) {
}
