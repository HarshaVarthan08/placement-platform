package com.placement.platform.premium.service;

import com.placement.platform.entity.User;
import com.placement.platform.premium.dto.SubscriptionResponseDto;
import com.placement.platform.premium.entity.Subscription;
import com.placement.platform.premium.enums.SubscriptionType;

public interface SubscriptionService {
    Subscription getActiveSubscription(User user);
    SubscriptionResponseDto getActiveSubscriptionDto(User user);
    SubscriptionResponseDto upgradeSubscription(Long userId, SubscriptionType type, Integer durationDays);
    SubscriptionResponseDto downgradeSubscription(Long userId);
    void initializeFreeSubscription(User user);
    SubscriptionResponseDto getSubscriptionDtoByUserId(Long userId);
}
