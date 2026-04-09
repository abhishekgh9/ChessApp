package com.chess.demo.service;

import java.util.List;
import java.util.Set;

public interface ChessEngineService {

    PositionInfo describePosition(List<String> uciMoves);

    PositionInfo describeFen(String fen);

    String findBestMove(List<String> uciMoves, int level);

    AnalysisInfo analyzePosition(List<String> uciMoves);

    AnalysisInfo analyzeFen(String fen);

    record PositionInfo(String fen, Set<String> legalMoves) {
    }

    record AnalysisInfo(String bestMove, Double evaluation, List<String> principalVariation) {
    }
}
