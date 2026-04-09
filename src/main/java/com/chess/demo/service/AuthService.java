package com.chess.demo.service;

import com.chess.demo.common.ApiException;
import com.chess.demo.dto.auth.AuthResponse;
import com.chess.demo.dto.auth.LoginRequest;
import com.chess.demo.dto.auth.RegisterRequest;
import com.chess.demo.entity.User;
import com.chess.demo.entity.UserSettings;
import com.chess.demo.repository.UserRepository;
import com.chess.demo.security.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserMapper userMapper;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtUtil jwtUtil,
                       UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userMapper = userMapper;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.findByUsernameIgnoreCase(request.username()).isPresent()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "username_taken");
        }
        if (userRepository.findByEmailIgnoreCase(request.email()).isPresent()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "email_taken");
        }

        User user = new User();
        user.setUsername(request.username().trim());
        user.setEmail(request.email().trim().toLowerCase());
        user.setPasswordHash(passwordEncoder.encode(request.password()));

        UserSettings settings = new UserSettings();
        settings.setUser(user);
        user.setSettings(settings);

        User saved = userRepository.save(user);
        return new AuthResponse(
                jwtUtil.generateToken(saved.getUsername()),
                userMapper.toUserSummary(saved),
                userMapper.toSettingsResponse(saved.getSettings())
        );
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmailIgnoreCase(request.email().trim())
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "invalid_credentials"));

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), request.password())
            );
        } catch (AuthenticationException exception) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "invalid_credentials");
        }

        return new AuthResponse(
                jwtUtil.generateToken(user.getUsername()),
                userMapper.toUserSummary(user),
                userMapper.toSettingsResponse(user.getSettings())
        );
    }

    @Transactional(readOnly = true)
    public AuthResponse me(User user, String authorizationHeader) {
        return new AuthResponse(
                resolveTokenForMe(user, authorizationHeader),
                userMapper.toUserSummary(user),
                userMapper.toSettingsResponse(user.getSettings())
        );
    }

    private String resolveTokenForMe(User user, String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.regionMatches(true, 0, "Bearer ", 0, 7)) {
            String token = authorizationHeader.substring(7).trim();
            if (!token.isBlank()) {
                return token;
            }
        }
        return jwtUtil.generateToken(user.getUsername());
    }
}
