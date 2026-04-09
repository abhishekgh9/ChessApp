package com.chess.demo.dto.chat;

import java.time.Instant;
import java.util.UUID;

public record GameChatMessageResponse(
        UUID id,
        UUID gameId,
        UUID senderId,
        String senderUsername,
        String message,
        Instant createdAt
) {
}
