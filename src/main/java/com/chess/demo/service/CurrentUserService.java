package com.chess.demo.service;

import com.chess.demo.common.ApiException;
import com.chess.demo.entity.User;
import com.chess.demo.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class CurrentUserService {

    private final UserRepository userRepository;

    public CurrentUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User requireUser(Principal principal) {
        if (principal == null || principal.getName() == null) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "unauthorized");
        }

        return userRepository.findByUsernameIgnoreCase(principal.getName())
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "unauthorized"));
    }
}
