package com.luckspinquest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/users")
public class AdminUserController {

    @GetMapping
    public ResponseEntity<?> getUsers() {
        return ResponseEntity.ok(
                Map.of("message", "Admin users")
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(
                Map.of(
                        "message", "Admin user detail",
                        "id", id
                )
        );
    }

    @PostMapping
    public ResponseEntity<?> createUser() {
        return ResponseEntity.ok(
                Map.of("message", "Admin user created")
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id) {
        return ResponseEntity.ok(
                Map.of(
                        "message", "Admin user updated",
                        "id", id
                )
        );
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> changeUserStatus(@PathVariable Long id) {
        return ResponseEntity.ok(
                Map.of(
                        "message", "User status changed",
                        "id", id
                )
        );
    }

    @PatchMapping("/{id}/role")
    public ResponseEntity<?> changeUserRole(@PathVariable Long id) {
        return ResponseEntity.ok(
                Map.of(
                        "message", "User role changed",
                        "id", id
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        return ResponseEntity.ok(
                Map.of(
                        "message", "Admin user deleted",
                        "id", id
                )
        );
    }
}
