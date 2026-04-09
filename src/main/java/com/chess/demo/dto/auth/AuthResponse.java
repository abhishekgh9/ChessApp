package com.chess.demo.dto.auth;

import com.chess.demo.dto.settings.UserSettingsResponse;

public record AuthResponse(
        String token,
        UserSummaryResponse user,
        UserSettingsResponse settings
) {
}
