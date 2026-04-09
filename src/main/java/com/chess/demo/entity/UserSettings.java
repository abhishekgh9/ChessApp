package com.chess.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "user_settings")
@Getter
@Setter
public class UserSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "move_sounds", nullable = false)
    private Boolean moveSounds = true;

    @Column(name = "notification_sounds", nullable = false)
    private Boolean notificationSounds = true;

    @Column(name = "game_alerts", nullable = false)
    private Boolean gameAlerts = true;

    @Column(name = "chat_messages", nullable = false)
    private Boolean chatMessages = true;

    @Column(name = "board_theme", nullable = false, length = 30)
    private String boardTheme = "classic";

    @Column(name = "default_time_control", nullable = false, length = 15)
    private String defaultTimeControl = "10+0";

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @PrePersist
    void applyDefaults() {
        if (moveSounds == null) {
            moveSounds = true;
        }
        if (notificationSounds == null) {
            notificationSounds = true;
        }
        if (gameAlerts == null) {
            gameAlerts = true;
        }
        if (chatMessages == null) {
            chatMessages = true;
        }
        if (boardTheme == null || boardTheme.isBlank()) {
            boardTheme = "classic";
        }
        if (defaultTimeControl == null || defaultTimeControl.isBlank()) {
            defaultTimeControl = "10+0";
        }
    }
}
