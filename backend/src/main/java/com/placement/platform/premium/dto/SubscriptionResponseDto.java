package com.placement.platform.premium.dto;

import com.placement.platform.premium.enums.SubscriptionStatus;
import com.placement.platform.premium.enums.SubscriptionType;
import java.time.LocalDateTime;

public record SubscriptionResponseDto(
    Long id,
    Long userId,
    String userEmail,
    SubscriptionType subscriptionType,
    SubscriptionStatus subscriptionStatus,
    LocalDateTime startDate,
    LocalDateTime expiryDate,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
