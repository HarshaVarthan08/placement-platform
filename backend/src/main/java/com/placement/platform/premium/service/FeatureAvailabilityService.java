package com.placement.platform.premium.service;

import com.placement.platform.premium.dto.FeatureAvailability;
import com.placement.platform.premium.enums.PremiumFeature;

public interface FeatureAvailabilityService {
    FeatureAvailability getAvailability(PremiumFeature feature);
    boolean isAvailable(PremiumFeature feature);
}
