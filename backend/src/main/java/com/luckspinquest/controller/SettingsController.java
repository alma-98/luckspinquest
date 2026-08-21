package com.luckspinquest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/settings")
public class SettingsController {

    // =========================================================
    // GET /api/v1/settings/public
    // Public application settings
    // =========================================================
    @GetMapping("/public")
    public ResponseEntity<?> getPublicSettings() {

        return ResponseEntity.ok(
                Map.of(
                        "application", "LuckSpinQuest",
                        "message", "Public application settings"
                )
        );
    }
}
