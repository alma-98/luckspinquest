package com.luckspinquest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    // =========================================================
    // GET /api/v1/notifications
    // List notifications
    // =========================================================
    @GetMapping
    public ResponseEntity<?> getNotifications() {

        return ResponseEntity.ok(
                Map.of(
                        "message", "Notification list"
                )
        );
    }

    // =========================================================
    // PATCH /api/v1/notifications/{id}/read
    // Mark notification as read
    // =========================================================
    @PatchMapping("/{id}/read")
    public ResponseEntity<?> markAsRead(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                Map.of(
                        "message", "Notification marked as read",
                        "id", id
                )
        );
    }

    // =========================================================
    // PATCH /api/v1/notifications/read-all
    // Mark all notifications as read
    // =========================================================
    @PatchMapping("/read-all")
    public ResponseEntity<?> markAllAsRead() {

        return ResponseEntity.ok(
                Map.of(
                        "message", "All notifications marked as read"
                )
        );
    }

    // =========================================================
    // DELETE /api/v1/notifications/{id}
    // Delete notification
    // =========================================================
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNotification(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                Map.of(
                        "message", "Notification deleted",
                        "id", id
                )
        );
    }
}
