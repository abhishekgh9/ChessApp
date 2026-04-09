package com.chess.demo.repository;

import com.chess.demo.entity.Game;
import com.chess.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface GameRepository extends JpaRepository<Game, UUID> {

    List<Game> findTop10ByWhitePlayerOrBlackPlayerOrderByUpdatedAtDesc(User whitePlayer, User blackPlayer);

    long countByWhitePlayerOrBlackPlayer(User whitePlayer, User blackPlayer);
}
