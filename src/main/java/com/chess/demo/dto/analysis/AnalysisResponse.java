package com.chess.demo.dto.analysis;

import java.util.List;
import java.util.UUID;

public record AnalysisResponse(
        UUID analysisId,
        String sourceType,
        String bestMove,
        Double evaluation,
        List<Double> evaluationSeries,
        List<String> moveClassifications,
        Boolean valid
) {
}
