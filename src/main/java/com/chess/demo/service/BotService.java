package com.chess.demo.service;

import com.chess.demo.dto.bot.CreateBotGameRequest;
import com.chess.demo.dto.game.GameResponse;
import com.chess.demo.entity.Game;
import com.chess.demo.entity.User;
import com.chess.demo.repository.GameRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BotService {

    private final GameRepository gameRepository;
    private final GameService gameService;

    public BotService(GameRepository gameRepository, GameService gameService) {
        this.gameRepository = gameRepository;
        this.gameService = gameService;
    }

    @Transactional
    public GameResponse createBotGame(User user, CreateBotGameRequest request) {
        Game game = new Game();
        game.setBotGame(true);
        int requestedLevel = request == null || request.level() == null ? 1 : request.level();
        game.setBotLevel(Math.max(1, Math.min(10, requestedLevel)));
        game.setTimeControl(user.getSettings().getDefaultTimeControl());
        game.setWhiteTimeRemaining(600);
        game.setBlackTimeRemaining(600);

        String color = request == null || request.color() == null ? "white" : request.color().trim().toLowerCase();
        if ("black".equals(color)) {
            game.setBlackPlayer(user);
            game.setTurnColor("white");
        } else {
            game.setWhitePlayer(user);
            game.setTurnColor("white");
        }

        Game saved = gameRepository.save(game);
        return "black".equals(color)
                ? gameService.runBotTurnIfNeeded(saved.getId(), user)
                : gameService.toResponse(saved);
    }
}
