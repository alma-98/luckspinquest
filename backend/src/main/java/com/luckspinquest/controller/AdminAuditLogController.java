package com.luckspinquest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/audit-logs")
public class AdminAuditLogController {

    // =========================================================
    // GET /api/v1/admin/audit-logs
    // List audit logs
    // =========================================================
    @GetMapping
    public ResponseEntity<?> getAuditLogs() {
        return ResponseEntity.ok(
                Map.of(
                        "message", "Audit log list"
                )
        );
    }

    // =========================================================
    // GET /api/v1/admin/audit-logs/{id}
    // Get audit log detail
    // =========================================================
    @GetMapping("/{id}")
    public ResponseEntity<?> getAuditLog(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                Map.of(
                        "message", "Audit log detail",
                        "id", id
                )
        );
    }

    // =========================================================
    // GET /api/v1/admin/audit-logs/user/{userId}
    // Get user audit history
    // =========================================================
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserAuditHistory(
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(
                Map.of(
                        "message", "User audit history",
                        "userId", userId
                )
        );
    }
}
