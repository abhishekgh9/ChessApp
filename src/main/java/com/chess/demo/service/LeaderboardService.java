package com.chess.demo.service;

import com.chess.demo.dto.leaderboard.LeaderboardEntryResponse;
import com.chess.demo.entity.User;
import com.chess.demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class LeaderboardService {

    private final UserRepository userRepository;

    public LeaderboardService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<LeaderboardEntryResponse> getLeaderboard(String query) {
        List<User> users = (query == null || query.isBlank())
                ? userRepository.findTop50ByOrderByRatingDesc()
                : userRepository.findTop50ByUsernameContainingIgnoreCaseOrderByRatingDesc(query.trim());

        AtomicInteger counter = new AtomicInteger(1);
        return users.stream()
                .map(user -> new LeaderboardEntryResponse(
                        counter.getAndIncrement(),
                        user.getUsername(),
                        user.getTitle(),
                        user.getCountry(),
                        user.getRating(),
                        0,
                        user.getWinsCount() + user.getLossesCount() + user.getDrawsCount(),
                        calculateWinRate(user)
                ))
                .toList();
    }

    private double calculateWinRate(User user) {
        int total = user.getWinsCount() + user.getLossesCount() + user.getDrawsCount();
        if (total == 0) {
            return 0.0;
        }
        return Math.round(((double) user.getWinsCount() / total) * 1000.0) / 10.0;
    }
}
