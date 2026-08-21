package com.luckspinquest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/redemptions")
public class RedemptionController {

    // =========================================================
    // POST /api/v1/redemptions
    // Create reward redemption
    // =========================================================
    @PostMapping
    public ResponseEntity<?> createRedemption() {

        return ResponseEntity.ok(
                Map.of(
                        "message", "Redemption created"
                )
        );
    }

    // =========================================================
    // GET /api/v1/redemptions
    // List own redemptions
    // =========================================================
    @GetMapping
    public ResponseEntity<?> getRedemptions() {

        return ResponseEntity.ok(
                Map.of(
                        "message", "Redemption list"
                )
        );
    }

    // =========================================================
    // GET /api/v1/redemptions/{id}
    // Get redemption detail
    // =========================================================
    @GetMapping("/{id}")
    public ResponseEntity<?> getRedemption(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                Map.of(
                        "message", "Redemption detail",
                        "id", id
                )
        );
    }

    // =========================================================
    // POST /api/v1/redemptions/{id}/cancel
    // Cancel redemption
    // =========================================================
    @PostMapping("/{id}/cancel")
    public ResponseEntity<?> cancelRedemption(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                Map.of(
                        "message", "Redemption cancelled",
                        "id", id
                )
        );
    }
}
