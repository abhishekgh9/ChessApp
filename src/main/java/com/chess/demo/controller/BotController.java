package com.chess.demo.controller;

import com.chess.demo.dto.bot.CreateBotGameRequest;
import com.chess.demo.dto.game.GameResponse;
import com.chess.demo.service.BotService;
import com.chess.demo.service.CurrentUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/bot-games")
public class BotController {

    private final BotService botService;
    private final CurrentUserService currentUserService;

    public BotController(BotService botService, CurrentUserService currentUserService) {
        this.botService = botService;
        this.currentUserService = currentUserService;
    }

    @PostMapping
    public ResponseEntity<GameResponse> createBotGame(Principal principal,
                                                      @RequestBody CreateBotGameRequest request) {
        return ResponseEntity.ok(botService.createBotGame(currentUserService.requireUser(principal), request));
    }
}
