package com.luckspinquest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/quests")
public class AdminQuestController {

    @GetMapping
    public ResponseEntity<?> getQuests() {
        return ResponseEntity.ok(
                Map.of("message", "Admin quest list")
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getQuest(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                Map.of(
                        "message", "Admin quest detail",
                        "id", id
                )
        );
    }

    @PostMapping
    public ResponseEntity<?> createQuest() {
        return ResponseEntity.ok(
                Map.of("message", "Quest created")
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateQuest(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                Map.of(
                        "message", "Quest updated",
                        "id", id
                )
        );
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> changeQuestStatus(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                Map.of(
                        "message", "Quest status changed",
                        "id", id
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteQuest(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                Map.of(
                        "message", "Quest deleted",
                        "id", id
                )
        );
    }
}
