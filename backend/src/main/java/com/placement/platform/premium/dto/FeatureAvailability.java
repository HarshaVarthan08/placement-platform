package com.placement.platform.premium.dto;

import com.placement.platform.premium.enums.FeatureStatus;
import com.placement.platform.premium.enums.PremiumFeature;

public record FeatureAvailability(
    PremiumFeature feature,
    boolean available,
    FeatureStatus status,
    String launchQuarter
) {}
