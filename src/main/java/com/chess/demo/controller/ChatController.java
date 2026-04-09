package com.chess.demo.controller;

import com.chess.demo.dto.chat.GameChatMessageResponse;
import com.chess.demo.dto.chat.SendChatMessageRequest;
import com.chess.demo.service.ChatService;
import com.chess.demo.service.CurrentUserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/games/{gameId}/chat")
public class ChatController {

    private final ChatService chatService;
    private final CurrentUserService currentUserService;

    public ChatController(ChatService chatService, CurrentUserService currentUserService) {
        this.chatService = chatService;
        this.currentUserService = currentUserService;
    }

    @GetMapping
    public ResponseEntity<List<GameChatMessageResponse>> getMessages(@PathVariable UUID gameId,
                                                                     Principal principal) {
        return ResponseEntity.ok(chatService.getMessages(gameId, currentUserService.requireUser(principal)));
    }

    @PostMapping
    public ResponseEntity<GameChatMessageResponse> sendMessage(@PathVariable UUID gameId,
                                                               Principal principal,
                                                               @Valid @RequestBody SendChatMessageRequest request) {
        return ResponseEntity.ok(chatService.sendMessage(gameId, currentUserService.requireUser(principal), request));
    }
}
