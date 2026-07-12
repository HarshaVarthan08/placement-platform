package com.placement.platform.health;

import com.placement.platform.career.config.CareerIntelligenceProperties;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class CareerIntelligenceEngineHealthIndicator implements HealthIndicator {
    private final CareerIntelligenceProperties properties;

    public CareerIntelligenceEngineHealthIndicator(CareerIntelligenceProperties properties) {
        this.properties = properties;
    }

    @Override
    public Health health() {
        if (properties != null && properties.getConfidence() != null) {
            int totalWeight = properties.getConfidence().getMatchWeight()
                    + properties.getConfidence().getPlacementWeight()
                    + properties.getConfidence().getResumeWeight()
                    + properties.getConfidence().getInterviewWeight();
            
            if (totalWeight == 100) {
                return Health.up()
                        .withDetail("status", "Initialized")
                        .withDetail("totalWeights", totalWeight)
                        .build();
            } else {
                return Health.down()
                        .withDetail("status", "Misconfigured")
                        .withDetail("totalWeights", totalWeight)
                        .build();
            }
        }
        return Health.down().withDetail("message", "Properties not loaded").build();
    }
}
