package com.chess.demo.repository;

import com.chess.demo.entity.Game;
import com.chess.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface GameRepository extends JpaRepository<Game, UUID> {

    List<Game> findTop10ByWhitePlayerOrBlackPlayerOrderByUpdatedAtDesc(User whitePlayer, User blackPlayer);

    long countByWhitePlayerOrBlackPlayer(User whitePlayer, User blackPlayer);

        @Query("""
                        select count(g)
                        from Game g
                        where g.status = 'FINISHED'
                            and ((g.whitePlayer = :user and g.result = 'WHITE_WIN')
                                or (g.blackPlayer = :user and g.result = 'BLACK_WIN'))
                        """)
        long countWinsForUser(@Param("user") User user);

        @Query("""
                        select count(g)
                        from Game g
                        where g.status = 'FINISHED'
                            and ((g.whitePlayer = :user and g.result = 'BLACK_WIN')
                                or (g.blackPlayer = :user and g.result = 'WHITE_WIN'))
                        """)
        long countLossesForUser(@Param("user") User user);

        @Query("""
                        select count(g)
                        from Game g
                        where g.status = 'FINISHED'
                            and g.result = 'DRAW'
                            and (g.whitePlayer = :user or g.blackPlayer = :user)
                        """)
        long countDrawsForUser(@Param("user") User user);
}
