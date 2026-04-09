package com.chess.demo.dto.matchmaking;

import java.util.UUID;

public record MatchmakingStatusResponse(
        Boolean searching,
        String timeControl,
        Boolean rated,
        UUID matchedGameId
) {
}
