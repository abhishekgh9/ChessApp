package com.chess.demo.repository;

import com.chess.demo.entity.User;
import com.chess.demo.entity.UserSettings;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserSettingsRepository extends JpaRepository<UserSettings, UUID> {

    Optional<UserSettings> findByUser(User user);
}
