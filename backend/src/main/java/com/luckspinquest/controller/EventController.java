package com.luckspinquest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/events")
public class EventController {

    // =====================================================
    // GET /api/v1/events
    // List events
    // =====================================================
    @GetMapping
    public ResponseEntity<?> getEvents() {
        return ResponseEntity.ok(
                Map.of("message", "Event list")
        );
    }

    // =====================================================
    // GET /api/v1/events/active
    // List active events
    // =====================================================
    @GetMapping("/active")
    public ResponseEntity<?> getActiveEvents() {
        return ResponseEntity.ok(
                Map.of("message", "Active event list")
        );
    }

    // =====================================================
    // GET /api/v1/events/upcoming
    // List upcoming events
    // =====================================================
    @GetMapping("/upcoming")
    public ResponseEntity<?> getUpcomingEvents() {
        return ResponseEntity.ok(
                Map.of("message", "Upcoming event list")
        );
    }

    // =====================================================
    // GET /api/v1/events/{id}
    // Get event detail
    // =====================================================
    @GetMapping("/{id}")
    public ResponseEntity<?> getEvent(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                Map.of(
                        "message", "Event detail",
                        "id", id
                )
        );
    }

    // =====================================================
    // POST /api/v1/events/{id}/join
    // Join event
    // =====================================================
    @PostMapping("/{id}/join")
    public ResponseEntity<?> joinEvent(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                Map.of(
                        "message", "Event joined",
                        "id", id
                )
        );
    }

    // =====================================================
    // GET /api/v1/events/{id}/leaderboard
    // Get event leaderboard
    // =====================================================
    @GetMapping("/{id}/leaderboard")
    public ResponseEntity<?> getEventLeaderboard(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                Map.of(
                        "message", "Event leaderboard",
                        "id", id
                )
        );
    }
}
