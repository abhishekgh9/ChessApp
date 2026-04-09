package com.chess.demo.dto.leaderboard;

public record LeaderboardEntryResponse(
        Integer rank,
        String username,
        String title,
        String country,
        Integer rating,
        Integer change,
        Integer gamesPlayed,
        Double winRate
) {
}
