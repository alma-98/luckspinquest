package com.luckspinquest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/withdrawals")
public class WithdrawalController {

    @GetMapping
    public ResponseEntity<?> getWithdrawals() {
        return ResponseEntity.ok(
                Map.of("message", "Withdrawal list")
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getWithdrawal(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                Map.of(
                        "message", "Withdrawal detail",
                        "id", id
                )
        );
    }

    @PostMapping
    public ResponseEntity<?> createWithdrawal() {
        return ResponseEntity.ok(
                Map.of("message", "Withdrawal request submitted")
        );
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<?> cancelWithdrawal(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                Map.of(
                        "message", "Withdrawal cancelled",
                        "id", id
                )
        );
    }
}
