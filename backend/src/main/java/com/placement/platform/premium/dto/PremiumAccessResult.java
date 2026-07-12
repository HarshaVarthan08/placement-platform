package com.placement.platform.premium.dto;

import com.placement.platform.premium.enums.FeatureStatus;
import com.placement.platform.premium.enums.SubscriptionType;

public record PremiumAccessResult(
    boolean allowed,
    String reason,
    SubscriptionType subscriptionType,
    FeatureStatus featureStatus
) {}
