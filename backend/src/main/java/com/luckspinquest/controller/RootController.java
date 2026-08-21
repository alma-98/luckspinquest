package com.luckspinquest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class RootController {

    @GetMapping("/")
    public ResponseEntity<?> root() {
        return ResponseEntity.ok(
                Map.of(
                        "application", "LuckSpinQuest",
                        "status", "UP",
                        "message", "LuckSpinQuest Backend API"
                )
        );
    }
}
