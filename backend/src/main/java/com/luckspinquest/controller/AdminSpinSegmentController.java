package com.luckspinquest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/spins/{spinId}/segments")
public class AdminSpinSegmentController {

    @GetMapping
    public ResponseEntity<?> getSegments(
            @PathVariable Long spinId) {

        return ResponseEntity.ok(
                Map.of(
                        "message", "Wheel segments",
                        "spinId", spinId
                )
        );
    }

    @PostMapping
    public ResponseEntity<?> createSegment(
            @PathVariable Long spinId) {

        return ResponseEntity.ok(
                Map.of(
                        "message", "Wheel segment created",
                        "spinId", spinId
                )
        );
    }

    @PutMapping("/{segmentId}")
    public ResponseEntity<?> updateSegment(
            @PathVariable Long spinId,
            @PathVariable Long segmentId) {

        return ResponseEntity.ok(
                Map.of(
                        "message", "Wheel segment updated",
                        "spinId", spinId,
                        "segmentId", segmentId
                )
        );
    }

    @DeleteMapping("/{segmentId}")
    public ResponseEntity<?> deleteSegment(
            @PathVariable Long spinId,
            @PathVariable Long segmentId) {

        return ResponseEntity.ok(
                Map.of(
                        "message", "Wheel segment deleted",
                        "spinId", spinId,
                        "segmentId", segmentId
                )
        );
    }
}
