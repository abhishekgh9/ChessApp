package com.chess.demo.service;

import com.chess.demo.dto.chat.GameChatMessageResponse;
import com.chess.demo.dto.chat.SendChatMessageRequest;
import com.chess.demo.entity.Game;
import com.chess.demo.entity.GameChatMessage;
import com.chess.demo.entity.User;
import com.chess.demo.repository.GameChatMessageRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ChatService {

    private final GameService gameService;
    private final GameChatMessageRepository gameChatMessageRepository;
    private final UserMapper userMapper;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatService(GameService gameService,
                       GameChatMessageRepository gameChatMessageRepository,
                       UserMapper userMapper,
                       SimpMessagingTemplate messagingTemplate) {
        this.gameService = gameService;
        this.gameChatMessageRepository = gameChatMessageRepository;
        this.userMapper = userMapper;
        this.messagingTemplate = messagingTemplate;
    }

    @Transactional(readOnly = true)
    public List<GameChatMessageResponse> getMessages(UUID gameId, User user) {
        Game game = gameService.getGameEntity(gameId);
        gameService.getGame(gameId, user);
        return gameChatMessageRepository.findByGameOrderByCreatedAtAsc(game)
                .stream()
                .map(userMapper::toChatResponse)
                .toList();
    }

    @Transactional
    public GameChatMessageResponse sendMessage(UUID gameId, User user, SendChatMessageRequest request) {
        Game game = gameService.getGameEntity(gameId);
        gameService.getGame(gameId, user);

        GameChatMessage message = new GameChatMessage();
        message.setGame(game);
        message.setSender(user);
        message.setMessage(request.message().trim());
        GameChatMessageResponse response = userMapper.toChatResponse(gameChatMessageRepository.save(message));
        messagingTemplate.convertAndSend("/topic/game/" + gameId, response);
        return response;
    }
}
