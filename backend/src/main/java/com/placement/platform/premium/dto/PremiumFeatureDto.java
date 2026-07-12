package com.placement.platform.premium.dto;

import com.placement.platform.premium.enums.FeatureStatus;

public record PremiumFeatureDto(
    String feature,
    String code,
    boolean premium,
    FeatureStatus status,
    String launch,
    String description,
    String category,
    String iconKey
) {}
