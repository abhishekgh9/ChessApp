package com.chess.demo.repository;

import com.chess.demo.entity.Game;
import com.chess.demo.entity.GameChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface GameChatMessageRepository extends JpaRepository<GameChatMessage, UUID> {

    List<GameChatMessage> findByGameOrderByCreatedAtAsc(Game game);
}
