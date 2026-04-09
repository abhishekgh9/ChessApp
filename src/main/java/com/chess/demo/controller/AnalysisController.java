package com.chess.demo.controller;

import com.chess.demo.dto.analysis.AnalyzeFenRequest;
import com.chess.demo.dto.analysis.AnalyzePgnRequest;
import com.chess.demo.dto.analysis.AnalysisResponse;
import com.chess.demo.service.AnalysisService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/analysis")
public class AnalysisController {

    private final AnalysisService analysisService;

    public AnalysisController(AnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    @PostMapping("/pgn")
    public ResponseEntity<AnalysisResponse> analyzePgn(@Valid @RequestBody AnalyzePgnRequest request) {
        return ResponseEntity.ok(analysisService.analyzePgn(request.pgn()));
    }

    @PostMapping("/fen")
    public ResponseEntity<AnalysisResponse> analyzeFen(@Valid @RequestBody AnalyzeFenRequest request) {
        return ResponseEntity.ok(analysisService.analyzeFen(request.fen()));
    }
}
