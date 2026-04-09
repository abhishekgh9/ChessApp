package com.chess.demo.controller;

import com.chess.demo.dto.chat.SendChatMessageRequest;
import com.chess.demo.dto.game.GameResponse;
import com.chess.demo.dto.game.MoveRequest;
import com.chess.demo.service.CurrentUserService;
import com.chess.demo.service.ChatService;
import com.chess.demo.service.GameService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.UUID;

@Controller
public class GameSocketController {

    private final GameService gameService;
    private final ChatService chatService;
    private final CurrentUserService currentUserService;

    public GameSocketController(GameService gameService,
                                ChatService chatService,
                                CurrentUserService currentUserService) {
        this.gameService = gameService;
        this.chatService = chatService;
        this.currentUserService = currentUserService;
    }

    @MessageMapping("/game.move")
    public GameResponse handleMove(@Payload SocketMoveRequest request, Principal principal) {
        return gameService.submitMove(request.gameId(), currentUserService.requireUser(principal), request.toMoveRequest());
    }

    @MessageMapping("/game.chat")
    public void handleChat(@Payload SocketChatRequest request, Principal principal) {
        chatService.sendMessage(request.gameId(), currentUserService.requireUser(principal), new SendChatMessageRequest(request.message()));
    }

    public record SocketMoveRequest(UUID gameId, String from, String to, String promotion) {
        public MoveRequest toMoveRequest() {
            return new MoveRequest(from, to, promotion);
        }
    }

    public record SocketChatRequest(UUID gameId, String message) {
    }
}
