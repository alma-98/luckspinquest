package com.luckspinquest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/spins")
public class AdminSpinController {

    @GetMapping
    public ResponseEntity<?> getSpins() {
        return ResponseEntity.ok(
                Map.of("message", "Admin spin configurations")
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSpin(@PathVariable Long id) {
        return ResponseEntity.ok(
                Map.of(
                        "message", "Admin spin configuration detail",
                        "id", id
                )
        );
    }

    @PostMapping
    public ResponseEntity<?> createSpin() {
        return ResponseEntity.ok(
                Map.of("message", "Spin configuration created")
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSpin(@PathVariable Long id) {
        return ResponseEntity.ok(
                Map.of(
                        "message", "Spin configuration updated",
                        "id", id
                )
        );
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> changeSpinStatus(@PathVariable Long id) {
        return ResponseEntity.ok(
                Map.of(
                        "message", "Spin status changed",
                        "id", id
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSpin(@PathVariable Long id) {
        return ResponseEntity.ok(
                Map.of(
                        "message", "Spin configuration deleted",
                        "id", id
                )
        );
    }
}
