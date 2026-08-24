package com.luckspinquest.dto.user.controller.admin;

import com.luckspinquest.dto.user.dto.spin.SpinRuleRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/spins/{spinId}/rules")
public class AdminSpinRuleController {

    @GetMapping
    public ResponseEntity<?> getRules(
            @PathVariable Long spinId
    ) {
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<?> createRule(
            @PathVariable Long spinId,
            @RequestBody SpinRuleRequest request
    ) {
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{ruleId}")
    public ResponseEntity<?> updateRule(
            @PathVariable Long spinId,
            @PathVariable Long ruleId,
            @RequestBody SpinRuleRequest request
    ) {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{ruleId}")
    public ResponseEntity<Void> deleteRule(
            @PathVariable Long spinId,
            @PathVariable Long ruleId
    ) {
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/validate")
    public ResponseEntity<?> validateProbability(
            @PathVariable Long spinId,
            @RequestBody List<SpinRuleRequest> rules
    ) {
        var total = rules.stream()
                .map(SpinRuleRequest::probability)
                .filter(java.util.Objects::nonNull)
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);

        if (total.compareTo(new java.math.BigDecimal("100.00")) != 0) {
            return ResponseEntity.badRequest()
                    .body(java.util.Map.of(
                            "valid", false,
                            "totalProbability", total,
                            "message", "Total probability must equal 100%"
                    ));
        }

        return ResponseEntity.ok(
                java.util.Map.of(
                        "valid", true,
                        "totalProbability", total
                )
        );
    }

    @PostMapping("/publish")
    public ResponseEntity<?> publishRuleVersion(
            @PathVariable Long spinId
    ) {
        /*
         * Publish must create an immutable SpinRuleVersion.
         *
         * Existing SpinRuleVersion service/repository should be wired here.
         */
        return ResponseEntity.ok(
                java.util.Map.of(
                        "spinId", spinId,
                        "status", "PUBLISH_REQUESTED"
                )
        );
    }
}
