package com.placement.platform.premium.controller;

import com.placement.platform.premium.dto.SubscriptionResponseDto;
import com.placement.platform.premium.enums.SubscriptionType;
import com.placement.platform.premium.service.SubscriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/subscription")
@Tag(name = "Administration", description = "Administrative and subscription management endpoints")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @PostMapping("/upgrade/{userId}")
    @Operation(summary = "Upgrade user subscription (Admin)", description = "Upgrades a user's subscription to PREMIUM or other types with an optional custom duration.", tags = { "Administration" })
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Subscription upgraded successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid upgrade request details"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Admin authorization required")
    })
    public ResponseEntity<SubscriptionResponseDto> upgrade(
            @PathVariable Long userId,
            @RequestParam SubscriptionType type,
            @RequestParam(required = false) Integer durationDays
    ) {
        SubscriptionResponseDto response = subscriptionService.upgradeSubscription(userId, type, durationDays);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/downgrade/{userId}")
    @Operation(summary = "Downgrade user subscription (Admin)", description = "Downgrades a user's subscription back to FREE.", tags = { "Administration" })
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Subscription downgraded successfully"),
        @ApiResponse(responseCode = "403", description = "Forbidden - Admin authorization required")
    })
    public ResponseEntity<SubscriptionResponseDto> downgrade(@PathVariable Long userId) {
        SubscriptionResponseDto response = subscriptionService.downgradeSubscription(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get user subscription status", description = "Retrieves the active subscription details and status of a specific user.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully retrieved subscription status details"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<SubscriptionResponseDto> getSubscriptionByUserId(@PathVariable Long userId) {
        SubscriptionResponseDto response = subscriptionService.getSubscriptionDtoByUserId(userId);
        return ResponseEntity.ok(response);
    }
}
