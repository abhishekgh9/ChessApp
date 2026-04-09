package com.chess.demo.service;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AnalysisServiceTest {

    @Test
    void analyzeFenReturnsEngineBackedResponse() {
        AnalysisService service = new AnalysisService(new FakeChessEngineService());

        var response = service.analyzeFen("8/8/8/8/8/8/8/K6k w - - 0 1");

        assertTrue(response.valid());
        assertEquals("FEN", response.sourceType());
        assertEquals("a1a2", response.bestMove());
        assertEquals(List.of(0.4), response.evaluationSeries());
        assertTrue(response.moveClassifications().isEmpty());
    }

    @Test
    void analyzePgnParsesSanAndClassifiesMoves() {
        AnalysisService service = new AnalysisService(new FakeChessEngineService());

        var response = service.analyzePgn("1. e4 e5 2. Nf3 Nc6");

        assertTrue(response.valid());
        assertEquals("PGN", response.sourceType());
        assertEquals(List.of(0.2, 0.1, -2.4, -1.95), response.evaluationSeries());
        assertEquals(List.of("best", "excellent", "blunder", "best"), response.moveClassifications());
        assertEquals("f1c4", response.bestMove());
    }

    @Test
    void analyzePgnSupportsStoredCoordinateHistory() {
        AnalysisService service = new AnalysisService(new FakeChessEngineService());

        var response = service.analyzePgn("e2-e4 e7-e5 g1f3 b8c6");

        assertTrue(response.valid());
        assertEquals(4, response.moveClassifications().size());
    }

    @Test
    void analyzePgnReturnsInvalidForBrokenNotation() {
        AnalysisService service = new AnalysisService(new FakeChessEngineService());

        var response = service.analyzePgn("1. e4 ???");

        assertFalse(response.valid());
        assertTrue(response.evaluationSeries().isEmpty());
        assertTrue(response.moveClassifications().isEmpty());
    }

    private static final class FakeChessEngineService implements ChessEngineService {

        private int analysisCalls;

        @Override
        public PositionInfo describePosition(List<String> uciMoves) {
            return new PositionInfo("startpos", Set.of());
        }

        @Override
        public PositionInfo describeFen(String fen) {
            return new PositionInfo(fen, Set.of());
        }

        @Override
        public String findBestMove(List<String> uciMoves, int level) {
            return null;
        }

        @Override
        public AnalysisInfo analyzePosition(List<String> uciMoves) {
            return analyzeFen("startpos");
        }

        @Override
        public AnalysisInfo analyzeFen(String fen) {
            if (fen.contains("K6k")) {
                return new AnalysisInfo("a1a2", 0.4, List.of("a1a2"));
            }

            return switch (analysisCalls++) {
                case 0 -> new AnalysisInfo("e2e4", 0.0, List.of("e2e4"));
                case 1 -> new AnalysisInfo("c7c5", 0.2, List.of("c7c5"));
                case 2 -> new AnalysisInfo("d2d4", 0.1, List.of("d2d4"));
                case 3 -> new AnalysisInfo("b8c6", -2.4, List.of("b8c6"));
                default -> new AnalysisInfo("f1c4", -1.95, List.of("f1c4"));
            };
        }
    }
}
