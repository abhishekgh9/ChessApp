package com.chess.demo.dto.matchmaking;

public record MatchmakingJoinRequest(
        String timeControl,
        Boolean rated
) {
}
