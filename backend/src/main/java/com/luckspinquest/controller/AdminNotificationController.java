package com.luckspinquest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/notifications")
public class AdminNotificationController {

    // =========================================================
    // POST /api/v1/admin/notifications
    // Create notification
    // =========================================================
    @PostMapping
    public ResponseEntity<?> createNotification() {

        return ResponseEntity.ok(
                Map.of(
                        "message", "Admin notification created"
                )
        );
    }

    // =========================================================
    // POST /api/v1/admin/notifications/broadcast
    // Broadcast notification
    // =========================================================
    @PostMapping("/broadcast")
    public ResponseEntity<?> broadcastNotification() {

        return ResponseEntity.ok(
                Map.of(
                        "message", "Notification broadcast"
                )
        );
    }

    // =========================================================
    // GET /api/v1/admin/notifications
    // List sent notifications
    // =========================================================
    @GetMapping
    public ResponseEntity<?> getNotifications() {

        return ResponseEntity.ok(
                Map.of(
                        "message", "Admin notification list"
                )
        );
    }
}
