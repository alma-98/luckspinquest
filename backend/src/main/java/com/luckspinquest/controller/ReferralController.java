package com.luckspinquest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/referrals")
public class ReferralController {

    // =========================================================
    // GET /api/v1/referrals
    // Get referrals
    // =========================================================
    @GetMapping
    public ResponseEntity<?> getReferrals() {

        return ResponseEntity.ok(
                Map.of(
                        "message", "Referral list"
                )
        );
    }

    // =========================================================
    // GET /api/v1/referrals/code
    // Get referral code
    // =========================================================
    @GetMapping("/code")
    public ResponseEntity<?> getReferralCode() {

        return ResponseEntity.ok(
                Map.of(
                        "message", "Referral code"
                )
        );
    }

    // =========================================================
    // GET /api/v1/referrals/statistics
    // Get referral statistics
    // =========================================================
    @GetMapping("/statistics")
    public ResponseEntity<?> getReferralStatistics() {

        return ResponseEntity.ok(
                Map.of(
                        "message", "Referral statistics"
                )
        );
    }

    // =========================================================
    // POST /api/v1/referrals/apply
    // Apply referral code
    // =========================================================
    @PostMapping("/apply")
    public ResponseEntity<?> applyReferralCode() {

        return ResponseEntity.ok(
                Map.of(
                        "message", "Referral code applied"
                )
        );
    }
}
