package com.chess.demo.service;

import com.chess.demo.common.ApiException;
import com.chess.demo.dto.game.GameResponse;
import com.chess.demo.dto.profile.AchievementResponse;
import com.chess.demo.dto.profile.ProfileSummaryResponse;
import com.chess.demo.entity.User;
import com.chess.demo.repository.GameRepository;
import com.chess.demo.repository.UserAchievementRepository;
import com.chess.demo.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class ProfileService {

    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final UserAchievementRepository userAchievementRepository;
    private final UserMapper userMapper;
    private final GameService gameService;

    public ProfileService(UserRepository userRepository,
                          GameRepository gameRepository,
                          UserAchievementRepository userAchievementRepository,
                          UserMapper userMapper,
                          GameService gameService) {
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
        this.userAchievementRepository = userAchievementRepository;
        this.userMapper = userMapper;
        this.gameService = gameService;
    }

    @Transactional(readOnly = true)
    public ProfileSummaryResponse getProfile(User requester) {
        return buildProfile(requester);
    }

    @Transactional(readOnly = true)
    public ProfileSummaryResponse getProfile(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "user_not_found"));
        return buildProfile(user);
    }

    @Transactional(readOnly = true)
    public java.util.List<GameResponse> getRecentGames(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "user_not_found"));
        return gameRepository.findTop10ByWhitePlayerOrBlackPlayerOrderByUpdatedAtDesc(user, user)
                .stream()
                .map(gameService::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public java.util.List<AchievementResponse> getAchievements(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "user_not_found"));
        return userAchievementRepository.findByUser(user)
                .stream()
                .map(userMapper::toAchievementResponse)
                .toList();
    }

    private ProfileSummaryResponse buildProfile(User user) {
        Map<String, Integer> ratings = Map.of(
                "rapid", user.getRating(),
                "blitz", Math.max(100, user.getRating() - 35),
                "bullet", Math.max(100, user.getRating() - 60)
        );
        Map<String, Integer> stats = new LinkedHashMap<>();
        stats.put("gamesPlayed", (int) gameRepository.countByWhitePlayerOrBlackPlayer(user, user));
        stats.put("wins", user.getWinsCount());
        stats.put("losses", user.getLossesCount());
        stats.put("draws", user.getDrawsCount());

        return new ProfileSummaryResponse(
                userMapper.toUserSummary(user),
                ratings,
                stats,
                gameRepository.findTop10ByWhitePlayerOrBlackPlayerOrderByUpdatedAtDesc(user, user)
                        .stream()
                        .map(gameService::toResponse)
                        .toList(),
                userAchievementRepository.findByUser(user)
                        .stream()
                        .map(userMapper::toAchievementResponse)
                        .toList()
        );
    }
}
