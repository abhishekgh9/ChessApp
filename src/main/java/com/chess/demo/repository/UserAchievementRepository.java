package com.chess.demo.repository;

import com.chess.demo.entity.User;
import com.chess.demo.entity.UserAchievement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserAchievementRepository extends JpaRepository<UserAchievement, UUID> {

    List<UserAchievement> findByUser(User user);
}
