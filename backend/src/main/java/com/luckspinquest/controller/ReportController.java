package com.luckspinquest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/reports")
public class ReportController {

    // =========================================================
    // GET /api/v1/reports/my-summary
    // User summary report
    // =========================================================
    @GetMapping("/my-summary")
    public ResponseEntity<?> getMySummary() {

        return ResponseEntity.ok(
                Map.of(
                        "message", "User summary report"
                )
        );
    }

    // =========================================================
    // GET /api/v1/reports/my-spin-history
    // User spin report
    // =========================================================
    @GetMapping("/my-spin-history")
    public ResponseEntity<?> getMySpinHistory() {

        return ResponseEntity.ok(
                Map.of(
                        "message", "User spin history report"
                )
        );
    }

    // =========================================================
    // GET /api/v1/reports/my-wallet-history
    // User wallet report
    // =========================================================
    @GetMapping("/my-wallet-history")
    public ResponseEntity<?> getMyWalletHistory() {

        return ResponseEntity.ok(
                Map.of(
                        "message", "User wallet history report"
                )
        );
    }
}
