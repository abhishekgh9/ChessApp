package com.chess.demo.dto.profile;

import java.time.Instant;
import java.util.UUID;

public record AchievementResponse(
        UUID id,
        String name,
        String description,
        Boolean earned,
        Instant earnedAt
) {
}
