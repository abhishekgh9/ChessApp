package com.chess.demo.controller;

import com.chess.demo.dto.game.GameResponse;
import com.chess.demo.dto.profile.AchievementResponse;
import com.chess.demo.dto.profile.ProfileSummaryResponse;
import com.chess.demo.service.CurrentUserService;
import com.chess.demo.service.ProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final ProfileService profileService;
    private final CurrentUserService currentUserService;

    public ProfileController(ProfileService profileService, CurrentUserService currentUserService) {
        this.profileService = profileService;
        this.currentUserService = currentUserService;
    }

    @GetMapping("/me")
    public ResponseEntity<ProfileSummaryResponse> me(Principal principal) {
        return ResponseEntity.ok(profileService.getProfile(currentUserService.requireUser(principal)));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ProfileSummaryResponse> byUserId(@PathVariable UUID userId) {
        return ResponseEntity.ok(profileService.getProfile(userId));
    }

    @GetMapping("/{userId}/games")
    public ResponseEntity<List<GameResponse>> recentGames(@PathVariable UUID userId) {
        return ResponseEntity.ok(profileService.getRecentGames(userId));
    }

    @GetMapping("/{userId}/achievements")
    public ResponseEntity<List<AchievementResponse>> achievements(@PathVariable UUID userId) {
        return ResponseEntity.ok(profileService.getAchievements(userId));
    }
}
