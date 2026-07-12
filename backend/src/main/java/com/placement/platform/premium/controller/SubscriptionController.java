package com.placement.platform.premium.controller;

import com.placement.platform.premium.dto.SubscriptionResponseDto;
import com.placement.platform.premium.enums.SubscriptionType;
import com.placement.platform.premium.service.SubscriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/subscription")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @PostMapping("/upgrade/{userId}")
    public ResponseEntity<SubscriptionResponseDto> upgrade(
            @PathVariable Long userId,
            @RequestParam SubscriptionType type,
            @RequestParam(required = false) Integer durationDays
    ) {
        SubscriptionResponseDto response = subscriptionService.upgradeSubscription(userId, type, durationDays);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/downgrade/{userId}")
    public ResponseEntity<SubscriptionResponseDto> downgrade(@PathVariable Long userId) {
        SubscriptionResponseDto response = subscriptionService.downgradeSubscription(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<SubscriptionResponseDto> getSubscriptionByUserId(@PathVariable Long userId) {
        SubscriptionResponseDto response = subscriptionService.getSubscriptionDtoByUserId(userId);
        return ResponseEntity.ok(response);
    }
}
