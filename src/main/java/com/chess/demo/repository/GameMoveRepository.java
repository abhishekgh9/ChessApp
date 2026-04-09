package com.chess.demo.repository;

import com.chess.demo.entity.Game;
import com.chess.demo.entity.GameMove;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface GameMoveRepository extends JpaRepository<GameMove, UUID> {

    List<GameMove> findByGameOrderByMoveNumberAsc(Game game);

    long countByGame(Game game);
}
