package com.chess.demo.dto.settings;

public record UpdateSettingsRequest(
        Boolean moveSounds,
        Boolean notificationSounds,
        Boolean gameAlerts,
        Boolean chatMessages,
        String boardTheme,
        String defaultTimeControl
) {
}
