package com.luckspinquest.dto.user.controller.admin;

import com.luckspinquest.dto.user.dto.spin.HybridSpinConstraintRequest;
import com.luckspinquest.dto.user.dto.spin.SpinControlRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/spin-controls")
public class AdminSpinControlController {

    @GetMapping
    public ResponseEntity<?> list() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<?> create(
            @RequestBody SpinControlRequest request
    ) {
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @RequestBody SpinControlRequest request
    ) {
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> changeStatus(
            @PathVariable Long id,
            @RequestParam boolean active
    ) {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id
    ) {
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/hybrid")
    public ResponseEntity<?> createHybridConstraint(
            @RequestBody HybridSpinConstraintRequest request
    ) {
        return ResponseEntity.ok().build();
    }
}
