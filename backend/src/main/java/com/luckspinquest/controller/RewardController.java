package com.luckspinquest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/rewards")
public class RewardController {

    @GetMapping
    public ResponseEntity<?> getRewards() {
        return ResponseEntity.ok(
                Map.of("message", "Reward list")
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getReward(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                Map.of(
                        "message", "Reward detail",
                        "id", id
                )
        );
    }

    @GetMapping("/categories")
    public ResponseEntity<?> getCategories() {
        return ResponseEntity.ok(
                Map.of("message", "Reward categories")
        );
    }

    @GetMapping("/featured")
    public ResponseEntity<?> getFeaturedRewards() {
        return ResponseEntity.ok(
                Map.of("message", "Featured rewards")
        );
    }
}
