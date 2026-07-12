package com.placement.platform.health;

import com.placement.platform.premium.config.PremiumProperties;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component("premiumModule")
public class PremiumModuleHealthIndicator implements HealthIndicator {
    private final PremiumProperties properties;

    public PremiumModuleHealthIndicator(PremiumProperties properties) {
        this.properties = properties;
    }

    @Override
    public Health health() {
        if (properties != null) {
            return Health.up()
                    .withDetail("enabled", properties.isEnabled())
                    .withDetail("launchQuarter", properties.getLaunchQuarter())
                    .withDetail("waitlistEnabled", properties.isWaitlistEnabled())
                    .build();
        }
        return Health.down().withDetail("message", "Premium properties not loaded").build();
    }
}
