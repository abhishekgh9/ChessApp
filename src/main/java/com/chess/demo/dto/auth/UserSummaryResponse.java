package com.chess.demo.dto.auth;

import java.time.Instant;
import java.util.UUID;

public record UserSummaryResponse(
        UUID id,
        String username,
        String email,
        Integer rating,
        String avatarUrl,
        String country,
        String title,
        Instant joinedAt
) {
}
