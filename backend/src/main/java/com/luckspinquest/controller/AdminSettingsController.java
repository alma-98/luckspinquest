package com.luckspinquest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/settings")
public class AdminSettingsController {

    // =========================================================
    // GET /api/v1/admin/settings
    // Get system settings
    // =========================================================
    @GetMapping
    public ResponseEntity<?> getSettings() {
        return ResponseEntity.ok(
                Map.of(
                        "message", "System settings"
                )
        );
    }

    // =========================================================
    // PUT /api/v1/admin/settings
    // Update system settings
    // =========================================================
    @PutMapping
    public ResponseEntity<?> updateSettings() {
        return ResponseEntity.ok(
                Map.of(
                        "message", "System settings updated"
                )
        );
    }

    // =========================================================
    // PATCH /api/v1/admin/settings/{key}
    // Update specific setting
    // =========================================================
    @PatchMapping("/{key}")
    public ResponseEntity<?> updateSetting(
            @PathVariable String key
    ) {
        return ResponseEntity.ok(
                Map.of(
                        "message", "Setting updated",
                        "key", key
                )
        );
    }
}
