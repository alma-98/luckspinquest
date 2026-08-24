package com.luckspinquest.controller;

import com.luckspinquest.dto.spin.PlaySpinRequest;
import com.luckspinquest.dto.spin.SpinResponse;
import com.luckspinquest.service.SpinService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/spins")
public class LuckySpinController {

    private final SpinService spinService;

    public LuckySpinController(SpinService spinService) {
        this.spinService = spinService;
    }

    @GetMapping
    public ResponseEntity<?> getSpins() {
        return ResponseEntity.ok(
                java.util.Map.of("message", "Available spin configurations")
        );
    }

    @GetMapping("/active")
    public ResponseEntity<?> getActiveSpin() {
        return ResponseEntity.ok(
                java.util.Map.of("message", "Active spin")
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSpin(@PathVariable Long id) {
        return ResponseEntity.ok(
                java.util.Map.of(
                        "message", "Spin detail",
                        "id", id
                )
        );
    }

    @PostMapping("/play")
    public ResponseEntity<SpinResponse> playSpin(
            @Valid @RequestBody PlaySpinRequest request,
            Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }

        Object details = authentication.getDetails();

        if (!(details instanceof Long userId)) {
            return ResponseEntity.status(401).build();
        }

        SpinResponse response = spinService.play(userId, request);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/history")
    public ResponseEntity<?> getSpinHistory() {
        return ResponseEntity.ok(
                java.util.Map.of("message", "Spin history")
        );
    }

    @GetMapping("/history/{id}")
    public ResponseEntity<?> getSpinHistoryDetail(@PathVariable Long id) {
        return ResponseEntity.ok(
                java.util.Map.of(
                        "message", "Spin result detail",
                        "id", id
                )
        );
    }

    @GetMapping("/available")
    public ResponseEntity<?> getAvailableSpins() {
        return ResponseEntity.ok(
                java.util.Map.of("message", "Available spins")
        );
    }
}
