package com.chess.demo.controller;

import com.chess.demo.dto.settings.UpdateSettingsRequest;
import com.chess.demo.dto.settings.UserSettingsResponse;
import com.chess.demo.service.CurrentUserService;
import com.chess.demo.service.SettingsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/settings")
public class SettingsController {

    private final SettingsService settingsService;
    private final CurrentUserService currentUserService;

    public SettingsController(SettingsService settingsService, CurrentUserService currentUserService) {
        this.settingsService = settingsService;
        this.currentUserService = currentUserService;
    }

    @GetMapping
    public ResponseEntity<UserSettingsResponse> getSettings(Principal principal) {
        return ResponseEntity.ok(settingsService.getSettings(currentUserService.requireUser(principal)));
    }

    @PatchMapping
    public ResponseEntity<UserSettingsResponse> updateSettings(Principal principal,
                                                               @RequestBody UpdateSettingsRequest request) {
        return ResponseEntity.ok(settingsService.updateSettings(currentUserService.requireUser(principal), request));
    }
}
