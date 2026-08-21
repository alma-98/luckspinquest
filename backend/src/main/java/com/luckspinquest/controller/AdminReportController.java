package com.luckspinquest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/reports")
public class AdminReportController {

    // =========================================================
    // GET /api/v1/admin/reports/overview
    // Overall platform report
    // =========================================================
    @GetMapping("/overview")
    public ResponseEntity<?> getOverview() {
        return ResponseEntity.ok(
                Map.of(
                        "message", "Overall platform report"
                )
        );
    }

    // =========================================================
    // GET /api/v1/admin/reports/users
    // User report
    // =========================================================
    @GetMapping("/users")
    public ResponseEntity<?> getUsersReport() {
        return ResponseEntity.ok(
                Map.of(
                        "message", "User report"
                )
        );
    }

    // =========================================================
    // GET /api/v1/admin/reports/spins
    // Spin report
    // =========================================================
    @GetMapping("/spins")
    public ResponseEntity<?> getSpinsReport() {
        return ResponseEntity.ok(
                Map.of(
                        "message", "Spin report"
                )
        );
    }

    // =========================================================
    // GET /api/v1/admin/reports/coins
    // Coin circulation report
    // =========================================================
    @GetMapping("/coins")
    public ResponseEntity<?> getCoinsReport() {
        return ResponseEntity.ok(
                Map.of(
                        "message", "Coin circulation report"
                )
        );
    }

    // =========================================================
    // GET /api/v1/admin/reports/rewards
    // Reward report
    // =========================================================
    @GetMapping("/rewards")
    public ResponseEntity<?> getRewardsReport() {
        return ResponseEntity.ok(
                Map.of(
                        "message", "Reward report"
                )
        );
    }

    // =========================================================
    // GET /api/v1/admin/reports/withdrawals
    // Withdrawal report
    // =========================================================
    @GetMapping("/withdrawals")
    public ResponseEntity<?> getWithdrawalsReport() {
        return ResponseEntity.ok(
                Map.of(
                        "message", "Withdrawal report"
                )
        );
    }

    // =========================================================
    // GET /api/v1/admin/reports/revenue
    // Revenue report
    // =========================================================
    @GetMapping("/revenue")
    public ResponseEntity<?> getRevenueReport() {
        return ResponseEntity.ok(
                Map.of(
                        "message", "Revenue report"
                )
        );
    }
}
