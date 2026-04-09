package com.chess.demo.service;

import com.chess.demo.dto.auth.UserSummaryResponse;
import com.chess.demo.dto.chat.GameChatMessageResponse;
import com.chess.demo.dto.game.GameResponse;
import com.chess.demo.dto.game.LastMoveResponse;
import com.chess.demo.dto.profile.AchievementResponse;
import com.chess.demo.dto.settings.UserSettingsResponse;
import com.chess.demo.entity.Game;
import com.chess.demo.entity.GameChatMessage;
import com.chess.demo.entity.User;
import com.chess.demo.entity.UserAchievement;
import com.chess.demo.entity.UserSettings;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapper {

    public UserSummaryResponse toUserSummary(User user) {
        return new UserSummaryResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRating(),
                user.getAvatarUrl(),
                user.getCountry(),
                user.getTitle(),
                user.getJoinedAt()
        );
    }

    public UserSettingsResponse toSettingsResponse(UserSettings settings) {
        return new UserSettingsResponse(
                settings.getMoveSounds(),
                settings.getNotificationSounds(),
                settings.getGameAlerts(),
                settings.getChatMessages(),
                settings.getBoardTheme(),
                settings.getDefaultTimeControl()
        );
    }

    public GameResponse toGameResponse(Game game, List<String> history) {
        LastMoveResponse lastMoveResponse = null;
        if (game.getLastMoveFrom() != null && game.getLastMoveTo() != null) {
            lastMoveResponse = new LastMoveResponse(game.getLastMoveFrom(), game.getLastMoveTo(), game.getLastMoveSan());
        }

        return new GameResponse(
                game.getId(),
                game.getWhitePlayer() == null ? null : game.getWhitePlayer().getId(),
                game.getBlackPlayer() == null ? null : game.getBlackPlayer().getId(),
                game.getFen(),
                game.getPgn(),
                history,
                game.getTimeControl(),
                game.getWhiteTimeRemaining(),
                game.getBlackTimeRemaining(),
                game.getStatus(),
                game.getResult(),
                game.getResultReason(),
                lastMoveResponse,
                game.getRated(),
                game.getBotGame(),
                game.getBotLevel(),
                game.getTurnColor(),
                game.getDrawOfferedBy(),
                game.getCreatedAt(),
                game.getUpdatedAt()
        );
    }

    public GameChatMessageResponse toChatResponse(GameChatMessage message) {
        return new GameChatMessageResponse(
                message.getId(),
                message.getGame().getId(),
                message.getSender().getId(),
                message.getSender().getUsername(),
                message.getMessage(),
                message.getCreatedAt()
        );
    }

    public AchievementResponse toAchievementResponse(UserAchievement userAchievement) {
        return new AchievementResponse(
                userAchievement.getAchievement().getId(),
                userAchievement.getAchievement().getName(),
                userAchievement.getAchievement().getDescription(),
                userAchievement.getEarned(),
                userAchievement.getEarnedAt()
        );
    }
}
