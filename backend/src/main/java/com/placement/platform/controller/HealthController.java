package com.placement.platform.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/health")
@Tag(name = "Administration", description = "Administrative and health endpoints")
public class HealthController {

    @GetMapping
    @Operation(summary = "Liveness probe", description = "Simple health check endpoint returning UP status.")
    @ApiResponse(responseCode = "200", description = "System is alive")
    public ResponseEntity<Map<String, String>> healthCheck() {
        return ResponseEntity.ok(Map.of("status", "UP", "message", "Application is running successfully"));
    }
}
