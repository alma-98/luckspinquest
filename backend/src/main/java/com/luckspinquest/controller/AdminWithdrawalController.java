package com.luckspinquest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/withdrawals")
public class AdminWithdrawalController {

    @GetMapping
    public ResponseEntity<?> getWithdrawals() {
        return ResponseEntity.ok(
                Map.of("message", "Admin withdrawal list")
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getWithdrawal(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                Map.of(
                        "message", "Admin withdrawal detail",
                        "id", id
                )
        );
    }

    @PatchMapping("/{id}/approve")
    public ResponseEntity<?> approveWithdrawal(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                Map.of(
                        "message", "Withdrawal approved",
                        "id", id
                )
        );
    }

    @PatchMapping("/{id}/reject")
    public ResponseEntity<?> rejectWithdrawal(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                Map.of(
                        "message", "Withdrawal rejected",
                        "id", id
                )
        );
    }

    @PatchMapping("/{id}/processing")
    public ResponseEntity<?> processingWithdrawal(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                Map.of(
                        "message", "Withdrawal marked as processing",
                        "id", id
                )
        );
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<?> completeWithdrawal(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                Map.of(
                        "message", "Withdrawal completed",
                        "id", id
                )
        );
    }
}
