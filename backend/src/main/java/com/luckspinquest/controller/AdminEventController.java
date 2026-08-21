package com.luckspinquest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/events")
public class AdminEventController {

    // =========================================================
    // GET /api/v1/admin/events
    // List events
    // =========================================================
    @GetMapping
    public ResponseEntity<?> getEvents() {

        return ResponseEntity.ok(
                Map.of(
                        "message", "Admin event list"
                )
        );
    }

    // =========================================================
    // GET /api/v1/admin/events/{id}
    // Get event detail
    // =========================================================
    @GetMapping("/{id}")
    public ResponseEntity<?> getEvent(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                Map.of(
                        "message", "Admin event detail",
                        "id", id
                )
        );
    }

    // =========================================================
    // POST /api/v1/admin/events
    // Create event
    // =========================================================
    @PostMapping
    public ResponseEntity<?> createEvent() {

        return ResponseEntity.ok(
                Map.of(
                        "message", "Event created"
                )
        );
    }

    // =========================================================
    // PUT /api/v1/admin/events/{id}
    // Update event
    // =========================================================
    @PutMapping("/{id}")
    public ResponseEntity<?> updateEvent(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                Map.of(
                        "message", "Event updated",
                        "id", id
                )
        );
    }

    // =========================================================
    // PATCH /api/v1/admin/events/{id}/status
    // Change event status
    // =========================================================
    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateEventStatus(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                Map.of(
                        "message", "Event status updated",
                        "id", id
                )
        );
    }

    // =========================================================
    // DELETE /api/v1/admin/events/{id}
    // Delete event
    // =========================================================
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEvent(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                Map.of(
                        "message", "Event deleted",
                        "id", id
                )
        );
    }
}
