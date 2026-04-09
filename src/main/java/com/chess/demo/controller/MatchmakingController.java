package com.chess.demo.controller;

import com.chess.demo.dto.matchmaking.MatchmakingJoinRequest;
import com.chess.demo.dto.matchmaking.MatchmakingStatusResponse;
import com.chess.demo.service.CurrentUserService;
import com.chess.demo.service.MatchmakingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/matchmaking")
public class MatchmakingController {

    private final MatchmakingService matchmakingService;
    private final CurrentUserService currentUserService;

    public MatchmakingController(MatchmakingService matchmakingService, CurrentUserService currentUserService) {
        this.matchmakingService = matchmakingService;
        this.currentUserService = currentUserService;
    }

    @PostMapping("/join")
    public ResponseEntity<MatchmakingStatusResponse> join(Principal principal,
                                                          @RequestBody MatchmakingJoinRequest request) {
        return ResponseEntity.ok(matchmakingService.join(currentUserService.requireUser(principal), request));
    }

    @PostMapping("/cancel")
    public ResponseEntity<MatchmakingStatusResponse> cancel(Principal principal) {
        return ResponseEntity.ok(matchmakingService.cancel(currentUserService.requireUser(principal)));
    }

    @GetMapping("/status")
    public ResponseEntity<MatchmakingStatusResponse> status(Principal principal) {
        return ResponseEntity.ok(matchmakingService.status(currentUserService.requireUser(principal)));
    }
}
