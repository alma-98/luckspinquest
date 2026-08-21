package com.luckspinquest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/leaderboards")
public class LeaderboardController {

    // =========================================================
    // GET /api/v1/leaderboards
    // Get leaderboard
    // =========================================================
    @GetMapping
    public ResponseEntity<?> getLeaderboard() {

        return ResponseEntity.ok(
                Map.of(
                        "message", "Leaderboard"
                )
        );
    }

    // =========================================================
    // GET /api/v1/leaderboards/daily
    // Daily leaderboard
    // =========================================================
    @GetMapping("/daily")
    public ResponseEntity<?> getDailyLeaderboard() {

        return ResponseEntity.ok(
                Map.of(
                        "message", "Daily leaderboard"
                )
        );
    }

    // =========================================================
    // GET /api/v1/leaderboards/weekly
    // Weekly leaderboard
    // =========================================================
    @GetMapping("/weekly")
    public ResponseEntity<?> getWeeklyLeaderboard() {

        return ResponseEntity.ok(
                Map.of(
                        "message", "Weekly leaderboard"
                )
        );
    }

    // =========================================================
    // GET /api/v1/leaderboards/monthly
    // Monthly leaderboard
    // =========================================================
    @GetMapping("/monthly")
    public ResponseEntity<?> getMonthlyLeaderboard() {

        return ResponseEntity.ok(
                Map.of(
                        "message", "Monthly leaderboard"
                )
        );
    }

    // =========================================================
    // GET /api/v1/leaderboards/me
    // Current user ranking
    // =========================================================
    @GetMapping("/me")
    public ResponseEntity<?> getMyRanking() {

        return ResponseEntity.ok(
                Map.of(
                        "message", "My leaderboard ranking"
                )
        );
    }
}
