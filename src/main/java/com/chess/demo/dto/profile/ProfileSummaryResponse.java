package com.chess.demo.dto.profile;

import com.chess.demo.dto.auth.UserSummaryResponse;
import com.chess.demo.dto.game.GameResponse;

import java.util.List;
import java.util.Map;

public record ProfileSummaryResponse(
        UserSummaryResponse user,
        Map<String, Integer> ratings,
        Map<String, Integer> aggregateStats,
        List<GameResponse> recentGames,
        List<AchievementResponse> achievements
) {
}
