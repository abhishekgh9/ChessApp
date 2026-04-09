package com.chess.demo.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(nullable = false)
    private Integer rating = 1500;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(length = 4)
    private String country;

    @Column(length = 20)
    private String title;

    @Column(name = "wins_count", nullable = false)
    private Integer winsCount = 0;

    @Column(name = "losses_count", nullable = false)
    private Integer lossesCount = 0;

    @Column(name = "draws_count", nullable = false)
    private Integer drawsCount = 0;

    @CreationTimestamp
    @Column(name = "joined_at", nullable = false, updatable = false)
    private Instant joinedAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private UserSettings settings;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserAchievement> achievements = new ArrayList<>();

    @PrePersist
    void applyDefaults() {
        if (rating == null) {
            rating = 1500;
        }
        if (winsCount == null) {
            winsCount = 0;
        }
        if (lossesCount == null) {
            lossesCount = 0;
        }
        if (drawsCount == null) {
            drawsCount = 0;
        }
    }
}
