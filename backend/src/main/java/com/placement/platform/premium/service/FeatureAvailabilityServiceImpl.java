package com.placement.platform.premium.service;

import com.placement.platform.premium.config.PremiumProperties;
import com.placement.platform.premium.dto.FeatureAvailability;
import com.placement.platform.premium.enums.FeatureStatus;
import com.placement.platform.premium.enums.PremiumFeature;
import org.springframework.stereotype.Service;

@Service
public class FeatureAvailabilityServiceImpl implements FeatureAvailabilityService {

    private final PremiumProperties properties;

    public FeatureAvailabilityServiceImpl(PremiumProperties properties) {
        this.properties = properties;
    }

    @Override
    public FeatureAvailability getAvailability(PremiumFeature feature) {
        boolean available = isAvailable(feature);
        // By default, since no AI features are implemented in Milestone 1, they are COMING_SOON.
        FeatureStatus status = available ? FeatureStatus.AVAILABLE : FeatureStatus.COMING_SOON;
        return new FeatureAvailability(feature, available, status, properties.getLaunchQuarter());
    }

    @Override
    public boolean isAvailable(PremiumFeature feature) {
        // Globally controlled by premium.enabled property. In Milestone 1, premium AI is not implemented.
        if (!properties.isEnabled()) {
            return false;
        }
        // Design for future extension where certain features can be enabled.
        return false;
    }
}
