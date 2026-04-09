package com.chess.demo.service;

import com.chess.demo.dto.settings.UpdateSettingsRequest;
import com.chess.demo.dto.settings.UserSettingsResponse;
import com.chess.demo.entity.User;
import com.chess.demo.entity.UserSettings;
import com.chess.demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SettingsService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public SettingsService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Transactional(readOnly = true)
    public UserSettingsResponse getSettings(User user) {
        return userMapper.toSettingsResponse(user.getSettings());
    }

    @Transactional
    public UserSettingsResponse updateSettings(User user, UpdateSettingsRequest request) {
        UserSettings settings = user.getSettings();
        if (request.moveSounds() != null) {
            settings.setMoveSounds(request.moveSounds());
        }
        if (request.notificationSounds() != null) {
            settings.setNotificationSounds(request.notificationSounds());
        }
        if (request.gameAlerts() != null) {
            settings.setGameAlerts(request.gameAlerts());
        }
        if (request.chatMessages() != null) {
            settings.setChatMessages(request.chatMessages());
        }
        if (request.boardTheme() != null && !request.boardTheme().isBlank()) {
            settings.setBoardTheme(request.boardTheme().trim());
        }
        if (request.defaultTimeControl() != null && !request.defaultTimeControl().isBlank()) {
            settings.setDefaultTimeControl(request.defaultTimeControl().trim());
        }
        userRepository.save(user);
        return userMapper.toSettingsResponse(settings);
    }
}
