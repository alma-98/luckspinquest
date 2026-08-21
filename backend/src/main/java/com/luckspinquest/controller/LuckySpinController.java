package com.luckspinquest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/spins")
public class LuckySpinController {

    @GetMapping
    public ResponseEntity<?> getSpins() {
        return ResponseEntity.ok(
                Map.of("message", "Available spin configurations")
        );
    }

    @GetMapping("/active")
    public ResponseEntity<?> getActiveSpin() {
        return ResponseEntity.ok(
                Map.of("message", "Active spin")
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSpin(@PathVariable Long id) {
        return ResponseEntity.ok(
                Map.of(
                        "message", "Spin detail",
                        "id", id
                )
        );
    }

    @PostMapping("/play")
    public ResponseEntity<?> playSpin() {
        return ResponseEntity.ok(
                Map.of("message", "Lucky spin played")
        );
    }

    @GetMapping("/history")
    public ResponseEntity<?> getSpinHistory() {
        return ResponseEntity.ok(
                Map.of("message", "Spin history")
        );
    }

    @GetMapping("/history/{id}")
    public ResponseEntity<?> getSpinHistoryDetail(@PathVariable Long id) {
        return ResponseEntity.ok(
                Map.of(
                        "message", "Spin result detail",
                        "id", id
                )
        );
    }

    @GetMapping("/available")
    public ResponseEntity<?> getAvailableSpins() {
        return ResponseEntity.ok(
                Map.of("message", "Available spins")
        );
    }
}
