package com.luckspinquest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/dashboard")
public class DashboardController {

    @GetMapping
    public ResponseEntity<?> getDashboard() {
        return ResponseEntity.ok(
                Map.of(
                        "message", "User dashboard summary"
                )
        );
    }
}
