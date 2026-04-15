package com.chess.demo.service;

import com.chess.demo.common.ApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Service
public class StockfishEngineService implements ChessEngineService {

    private static final Pattern SCORE_PATTERN = Pattern.compile("\\bscore\\s+(cp|mate)\\s+(-?\\d+)");
    private static final Pattern PV_PATTERN = Pattern.compile("\\bpv\\s+(.+)$");

    private final String executablePath;
    private final long commandTimeoutMs;
    private final int analysisMoveTimeMs;

    public StockfishEngineService(
            @Value("${bot.stockfish.path:stockfish}") String executablePath,
            @Value("${bot.stockfish.command-timeout-ms:5000}") long commandTimeoutMs,
            @Value("${analysis.stockfish.movetime-ms:900}") int analysisMoveTimeMs) {
        this.executablePath = executablePath;
        this.commandTimeoutMs = commandTimeoutMs;
        this.analysisMoveTimeMs = analysisMoveTimeMs;
    }

    @Override
    public PositionInfo describePosition(List<String> uciMoves) {
        try (EngineSession session = openSession()) {
            session.initialize();
            session.setPosition(uciMoves);
            return session.describePosition();
        } catch (IOException exception) {
            throw new ApiException(HttpStatus.SERVICE_UNAVAILABLE, "stockfish_unavailable");
        }
    }

    @Override
    public PositionInfo describeFen(String fen) {
        try (EngineSession session = openSession()) {
            session.initialize();
            session.setPositionFen(fen);
            return session.describePosition();
        } catch (IOException exception) {
            throw new ApiException(HttpStatus.SERVICE_UNAVAILABLE, "stockfish_unavailable");
        }
    }

    @Override
    public String findBestMove(List<String> uciMoves, int level) {
        try (EngineSession session = openSession()) {
            session.initialize();
            session.setPosition(uciMoves);
            return session.findBestMove(level);
        } catch (IOException exception) {
            throw new ApiException(HttpStatus.SERVICE_UNAVAILABLE, "stockfish_unavailable");
        }
    }

    @Override
    public AnalysisInfo analyzePosition(List<String> uciMoves) {
        try (EngineSession session = openSession()) {
            session.initialize();
            session.setPosition(uciMoves);
            return session.analyze(analysisMoveTimeMs);
        } catch (IOException exception) {
            throw new ApiException(HttpStatus.SERVICE_UNAVAILABLE, "stockfish_unavailable");
        }
    }

    @Override
    public AnalysisInfo analyzeFen(String fen) {
        try (EngineSession session = openSession()) {
            session.initialize();
            session.setPositionFen(fen);
            return session.analyze(analysisMoveTimeMs);
        } catch (IOException exception) {
            throw new ApiException(HttpStatus.SERVICE_UNAVAILABLE, "stockfish_unavailable");
        }
    }

    private EngineSession openSession() throws IOException {
        IOException lastFailure = null;
        for (String candidate : resolveExecutableCandidates()) {
            try {
                return EngineSession.start(candidate, commandTimeoutMs);
            } catch (IOException exception) {
                lastFailure = exception;
            }
        }
        if (lastFailure != null) {
            throw lastFailure;
        }
        throw new IOException("Stockfish executable not found");
    }

    private List<String> resolveExecutableCandidates() {
        List<String> candidates = new ArrayList<>();
        addCandidate(candidates, executablePath);
        addCandidate(candidates, System.getenv("BOT_STOCKFISH_PATH"));
        addCandidate(candidates, "stockfish");

        String localAppData = System.getenv("LOCALAPPDATA");
        if (localAppData != null && !localAppData.isBlank()) {
            Path wingetPackageRoot = Path.of(localAppData, "Microsoft", "WinGet", "Packages");
            if (Files.isDirectory(wingetPackageRoot)) {
                try (Stream<Path> paths = Files.walk(wingetPackageRoot, 5)) {
                    paths.filter(Files::isRegularFile)
                            .map(Path::toString)
                            .filter(path -> path.toLowerCase().contains("stockfish") && path.toLowerCase().endsWith(".exe"))
                            .findFirst()
                            .ifPresent(path -> addCandidate(candidates, path));
                } catch (IOException ignored) {
                    // Best-effort fallback scan only.
                }
            }
        }
        return candidates;
    }

    private void addCandidate(List<String> candidates, String value) {
        if (value == null || value.isBlank()) {
            return;
        }
        if (!candidates.contains(value)) {
            candidates.add(value);
        }
    }

    private static final class EngineSession implements AutoCloseable {
        private final Process process;
        private final BufferedWriter writer;
        private final BufferedReader reader;
        private final long commandTimeoutMs;

        private EngineSession(Process process,
                              BufferedWriter writer,
                              BufferedReader reader,
                              long commandTimeoutMs) {
            this.process = process;
            this.writer = writer;
            this.reader = reader;
            this.commandTimeoutMs = commandTimeoutMs;
        }

        static EngineSession start(String executablePath, long commandTimeoutMs) throws IOException {
            Process process = new ProcessBuilder(executablePath)
                    .redirectErrorStream(true)
                    .start();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream(), StandardCharsets.UTF_8));
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));
            return new EngineSession(process, writer, reader, commandTimeoutMs);
        }

        void initialize() throws IOException {
            send("uci");
            readUntil(line -> "uciok".equals(line.trim()), commandTimeoutMs);
            send("isready");
            readUntil(line -> "readyok".equals(line.trim()), commandTimeoutMs);
        }

        void setPosition(List<String> uciMoves) throws IOException {
            resetGameState();
            if (uciMoves == null || uciMoves.isEmpty()) {
                send("position startpos");
                return;
            }
            send("position startpos moves " + String.join(" ", uciMoves));
        }

        void setPositionFen(String fen) throws IOException {
            resetGameState();
            send("position fen " + fen);
        }

        private void resetGameState() throws IOException {
            send("ucinewgame");
            send("isready");
            readUntil(line -> "readyok".equals(line.trim()), commandTimeoutMs);
        }

        PositionInfo describePosition() throws IOException {
            PositionSnapshot snapshot = readPositionSnapshot();
            if (snapshot == null || snapshot.fen() == null) {
                throw new ApiException(HttpStatus.BAD_GATEWAY, "stockfish_no_fen");
            }
            Set<String> legalMoves = collectLegalMovesWithPerft();
            return new PositionInfo(snapshot.fen(), legalMoves, snapshot.inCheck());
        }

        AnalysisInfo analyze(int moveTimeMs) throws IOException {
            String fen = readCurrentFen();
            if (fen == null) {
                throw new ApiException(HttpStatus.BAD_GATEWAY, "stockfish_no_fen");
            }
            boolean whiteToMove = isWhiteToMove(fen);

            send("go movetime " + moveTimeMs);
            List<String> output = readUntilCollect(line -> line.trim().startsWith("bestmove"), commandTimeoutMs + moveTimeMs + 4000L);

            RawAnalysis latest = null;
            String bestMove = null;
            for (String line : output) {
                String trimmed = line.trim();
                if (trimmed.startsWith("info ")) {
                    RawAnalysis parsed = parseAnalysis(trimmed);
                    if (parsed != null) {
                        latest = parsed;
                    }
                    continue;
                }
                if (trimmed.startsWith("bestmove")) {
                    String[] parts = trimmed.split("\\s+");
                    if (parts.length >= 2) {
                        String candidate = parts[1].trim().toLowerCase();
                        bestMove = "(none)".equals(candidate) ? null : candidate;
                    }
                }
            }

            Double evaluation = latest == null ? null : latest.toWhitePerspective(whiteToMove);
            List<String> principalVariation = latest == null ? List.of() : latest.principalVariation();
            return new AnalysisInfo(bestMove, evaluation, principalVariation);
        }

        private Set<String> collectLegalMovesWithPerft() throws IOException {
            Set<String> legalMoves = new HashSet<>();
            send("go perft 1");
            List<String> output = readUntilCollect(line -> line.trim().startsWith("Nodes searched:"), commandTimeoutMs + 2000);
            for (String line : output) {
                String trimmed = line.trim().toLowerCase();
                int separator = trimmed.indexOf(':');
                if (separator <= 0) {
                    continue;
                }
                String move = trimmed.substring(0, separator).trim();
                if (move.matches("^[a-h][1-8][a-h][1-8][qrbn]?$")) {
                    legalMoves.add(move);
                }
            }
            return legalMoves;
        }

        String findBestMove(int level) throws IOException {
            send("go movetime " + levelToMoveTimeMs(level));
            String bestMoveLine = readUntil(line -> line.trim().startsWith("bestmove"), commandTimeoutMs + 4000);
            String[] parts = bestMoveLine.trim().split("\\s+");
            if (parts.length < 2) {
                throw new ApiException(HttpStatus.BAD_GATEWAY, "stockfish_invalid_response");
            }
            String bestMove = parts[1].trim().toLowerCase();
            return "(none)".equals(bestMove) ? null : bestMove;
        }

        private String readCurrentFen() throws IOException {
            PositionSnapshot snapshot = readPositionSnapshot();
            return snapshot == null ? null : snapshot.fen();
        }

        private PositionSnapshot readPositionSnapshot() throws IOException {
            send("d");
            send("isready");
            List<String> output = readUntilCollect(line -> "readyok".equals(line.trim()), commandTimeoutMs);
            String fen = null;
            boolean inCheck = false;
            for (String line : output) {
                String trimmed = line.trim();
                if (trimmed.startsWith("Fen:")) {
                    fen = trimmed.substring("Fen:".length()).trim();
                }
                if (trimmed.startsWith("Checkers:")) {
                    String checkers = trimmed.substring("Checkers:".length()).trim();
                    inCheck = !checkers.isBlank() && !"-".equals(checkers);
                }
            }
            return fen == null ? null : new PositionSnapshot(fen, inCheck);
        }

        private boolean isWhiteToMove(String fen) {
            String[] parts = fen.trim().split("\\s+");
            return parts.length > 1 && "w".equalsIgnoreCase(parts[1]);
        }

        private RawAnalysis parseAnalysis(String line) {
            Matcher scoreMatcher = SCORE_PATTERN.matcher(line);
            if (!scoreMatcher.find()) {
                return null;
            }

            String scoreType = scoreMatcher.group(1);
            int rawScore = Integer.parseInt(scoreMatcher.group(2));
            Matcher pvMatcher = PV_PATTERN.matcher(line);
            List<String> pv = List.of();
            if (pvMatcher.find()) {
                pv = List.of(pvMatcher.group(1).trim().split("\\s+"));
            }

            if ("mate".equals(scoreType)) {
                return RawAnalysis.forMate(rawScore, pv);
            }
            return RawAnalysis.forCentipawns(rawScore, pv);
        }

        private int levelToMoveTimeMs(int level) {
            int bounded = Math.max(1, Math.min(10, level));
            return switch (bounded) {
                case 1 -> 80;
                case 2 -> 120;
                case 3 -> 180;
                case 4 -> 250;
                case 5 -> 350;
                case 6 -> 500;
                case 7 -> 700;
                case 8 -> 950;
                case 9 -> 1250;
                default -> 1600;
            };
        }

        private void send(String command) throws IOException {
            writer.write(command);
            writer.newLine();
            writer.flush();
        }

        private String readUntil(LineMatcher matcher, long timeoutMs) throws IOException {
            Instant deadline = Instant.now().plus(Duration.ofMillis(timeoutMs));
            while (Instant.now().isBefore(deadline)) {
                if (reader.ready()) {
                    String line = reader.readLine();
                    if (line == null) {
                        break;
                    }
                    if (matcher.matches(line)) {
                        return line;
                    }
                } else {
                    if (!process.isAlive()) {
                        break;
                    }
                    sleepBriefly();
                }
            }
            throw new ApiException(HttpStatus.BAD_GATEWAY, "stockfish_timeout");
        }

        private List<String> readUntilCollect(LineMatcher matcher, long timeoutMs) throws IOException {
            Instant deadline = Instant.now().plus(Duration.ofMillis(timeoutMs));
            List<String> lines = new ArrayList<>();
            while (Instant.now().isBefore(deadline)) {
                if (reader.ready()) {
                    String line = reader.readLine();
                    if (line == null) {
                        break;
                    }
                    lines.add(line);
                    if (matcher.matches(line)) {
                        return lines;
                    }
                } else {
                    if (!process.isAlive()) {
                        break;
                    }
                    sleepBriefly();
                }
            }
            throw new ApiException(HttpStatus.BAD_GATEWAY, "stockfish_timeout");
        }

        private void sleepBriefly() {
            try {
                Thread.sleep(10);
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
                throw new ApiException(HttpStatus.BAD_GATEWAY, "stockfish_interrupted");
            }
        }

        @Override
        public void close() {
            try {
                send("quit");
            } catch (Exception ignored) {
                // no-op
            }
            process.destroy();
        }

        @FunctionalInterface
        private interface LineMatcher {
            boolean matches(String line);
        }

        private record RawAnalysis(Integer centipawns, Integer mateIn, List<String> principalVariation) {

            static RawAnalysis forCentipawns(int centipawns, List<String> principalVariation) {
                return new RawAnalysis(centipawns, null, principalVariation);
            }

            static RawAnalysis forMate(int mateIn, List<String> principalVariation) {
                return new RawAnalysis(null, mateIn, principalVariation);
            }

            Double toWhitePerspective(boolean whiteToMove) {
                double evaluation = centipawns != null
                        ? centipawns / 100.0
                        : Math.copySign(Math.max(1.0, 100.0 - Math.min(99, Math.abs(mateIn))), mateIn);
                return whiteToMove ? evaluation : -evaluation;
            }
        }

        private record PositionSnapshot(String fen, boolean inCheck) {
        }
    }
}
