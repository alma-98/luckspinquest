package com.luckspinquest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/redemptions")
public class AdminRedemptionController {

    // =========================================================
    // GET /api/v1/admin/redemptions
    // List redemptions
    // =========================================================
    @GetMapping
    public ResponseEntity<?> getRedemptions() {
        return ResponseEntity.ok(
                Map.of(
                        "message", "Admin redemption list"
                )
        );
    }

    // =========================================================
    // GET /api/v1/admin/redemptions/{id}
    // Get redemption detail
    // =========================================================
    @GetMapping("/{id}")
    public ResponseEntity<?> getRedemption(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                Map.of(
                        "message", "Admin redemption detail",
                        "id", id
                )
        );
    }

    // =========================================================
    // PATCH /api/v1/admin/redemptions/{id}/approve
    // Approve redemption
    // =========================================================
    @PatchMapping("/{id}/approve")
    public ResponseEntity<?> approveRedemption(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                Map.of(
                        "message", "Redemption approved",
                        "id", id
                )
        );
    }

    // =========================================================
    // PATCH /api/v1/admin/redemptions/{id}/reject
    // Reject redemption
    // =========================================================
    @PatchMapping("/{id}/reject")
    public ResponseEntity<?> rejectRedemption(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                Map.of(
                        "message", "Redemption rejected",
                        "id", id
                )
        );
    }

    // =========================================================
    // PATCH /api/v1/admin/redemptions/{id}/complete
    // Complete redemption
    // =========================================================
    @PatchMapping("/{id}/complete")
    public ResponseEntity<?> completeRedemption(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                Map.of(
                        "message", "Redemption completed",
                        "id", id
                )
        );
    }
}
