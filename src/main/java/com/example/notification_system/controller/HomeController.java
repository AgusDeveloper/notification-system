package com.example.notification_system.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public Map<String, Object> home() {
        return Map.of(
                "application", "notification-system",
                "status", "running",
                "endpoints", Map.of(
                        "sendNotifications", "/notifications/send",
                        "health", "/actuator/health",
                        "readiness", "/actuator/health/readiness",
                        "bankUsers", "/bank/users",
                        "bankLoans", "/bank/loans"
                )
        );
    }
}
