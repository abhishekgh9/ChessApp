package com.chess.demo.service;

import com.chess.demo.dto.game.GameResponse;
import com.chess.demo.dto.matchmaking.MatchmakingJoinRequest;
import com.chess.demo.dto.matchmaking.MatchmakingStatusResponse;
import com.chess.demo.entity.User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class MatchmakingService {

    private final Map<UUID, Ticket> queue = new ConcurrentHashMap<>();
    private final Map<UUID, UUID> matchedGames = new ConcurrentHashMap<>();
    private final GameService gameService;

    public MatchmakingService(GameService gameService) {
        this.gameService = gameService;
    }

    public MatchmakingStatusResponse join(User user, MatchmakingJoinRequest request) {
        String timeControl = request.timeControl() == null || request.timeControl().isBlank()
                ? user.getSettings().getDefaultTimeControl()
                : request.timeControl().trim();
        boolean rated = Boolean.TRUE.equals(request.rated());

        for (Ticket existing : queue.values()) {
            if (!existing.userId().equals(user.getId())
                    && existing.timeControl().equalsIgnoreCase(timeControl)
                    && existing.rated() == rated) {
                queue.remove(existing.userId());
                queue.remove(user.getId());
                GameResponse game = gameService.createMatchedGame(existing.userId().compareTo(user.getId()) <= 0 ? existing.user() : user,
                        existing.userId().compareTo(user.getId()) <= 0 ? user : existing.user(),
                        timeControl,
                        rated);
                matchedGames.put(user.getId(), game.gameId());
                matchedGames.put(existing.userId(), game.gameId());
                return new MatchmakingStatusResponse(false, timeControl, rated, game.gameId());
            }
        }

        queue.put(user.getId(), new Ticket(user.getId(), user, timeControl, rated));
        return new MatchmakingStatusResponse(true, timeControl, rated, matchedGames.get(user.getId()));
    }

    public MatchmakingStatusResponse cancel(User user) {
        Ticket removed = queue.remove(user.getId());
        return new MatchmakingStatusResponse(false, removed == null ? null : removed.timeControl(), removed != null && removed.rated(), matchedGames.remove(user.getId()));
    }

    public MatchmakingStatusResponse status(User user) {
        Ticket ticket = queue.get(user.getId());
        return new MatchmakingStatusResponse(ticket != null, ticket == null ? null : ticket.timeControl(), ticket != null && ticket.rated(), matchedGames.get(user.getId()));
    }

    private record Ticket(UUID userId, User user, String timeControl, boolean rated) {
    }
}
