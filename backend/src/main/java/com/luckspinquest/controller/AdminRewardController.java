package com.luckspinquest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/rewards")
public class AdminRewardController {

    @GetMapping
    public ResponseEntity<?> getRewards() {
        return ResponseEntity.ok(
                Map.of("message", "Admin reward list")
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getReward(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                Map.of(
                        "message", "Admin reward detail",
                        "id", id
                )
        );
    }

    @PostMapping
    public ResponseEntity<?> createReward() {
        return ResponseEntity.ok(
                Map.of("message", "Reward created")
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateReward(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                Map.of(
                        "message", "Reward updated",
                        "id", id
                )
        );
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> changeRewardStatus(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                Map.of(
                        "message", "Reward status changed",
                        "id", id
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReward(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                Map.of(
                        "message", "Reward deleted",
                        "id", id
                )
        );
    }
}
