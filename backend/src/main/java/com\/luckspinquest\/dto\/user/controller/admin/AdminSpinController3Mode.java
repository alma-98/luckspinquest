package com.luckspinquest.dto.user.controller.admin;

import com.luckspinquest.dto.user.dto.spin.SpinModeRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/spins")
public class AdminSpinController3Mode {

    @PatchMapping("/{id}/mode")
    public ResponseEntity<Void> changeSpinMode(
            @PathVariable Long id,
            @RequestBody SpinModeRequest request
    ) {
        /*
         * Delegate to existing AdminSpinService.
         *
         * This endpoint is deliberately isolated from the existing
         * AdminSpinController to avoid overwriting existing behavior.
         */
        return ResponseEntity.noContent().build();
    }
}
