package com.placement.platform.health;

import com.placement.platform.job.matching.JobMatchingEngine;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class RecommendationEngineHealthIndicator implements HealthIndicator {
    private final JobMatchingEngine matchingEngine;

    public RecommendationEngineHealthIndicator(JobMatchingEngine matchingEngine) {
        this.matchingEngine = matchingEngine;
    }

    @Override
    public Health health() {
        if (matchingEngine != null) {
            return Health.up()
                    .withDetail("status", "Operational")
                    .build();
        }
        return Health.down().withDetail("message", "JobMatchingEngine not available").build();
    }
}
