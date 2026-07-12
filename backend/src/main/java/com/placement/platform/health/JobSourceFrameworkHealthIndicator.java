package com.placement.platform.health;

import com.placement.platform.job.service.source.JobSourceRegistry;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component("jobSourceFramework")
public class JobSourceFrameworkHealthIndicator implements HealthIndicator {
    private final JobSourceRegistry sourceRegistry;

    public JobSourceFrameworkHealthIndicator(JobSourceRegistry sourceRegistry) {
        this.sourceRegistry = sourceRegistry;
    }

    @Override
    public Health health() {
        if (sourceRegistry != null) {
            int registeredCount = sourceRegistry.getAllSources().size();
            return Health.up()
                    .withDetail("registeredSourcesCount", registeredCount)
                    .withDetail("status", "Active")
                    .build();
        }
        return Health.down().withDetail("message", "JobSourceRegistry not loaded").build();
    }
}
