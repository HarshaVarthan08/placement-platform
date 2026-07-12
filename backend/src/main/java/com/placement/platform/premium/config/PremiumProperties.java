package com.placement.platform.premium.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "premium")
public class PremiumProperties {
    private boolean enabled = false;
    private String launchQuarter = "Q4 2026";
    private boolean waitlistEnabled = true;
    private String defaultSubscription = "FREE";

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getLaunchQuarter() {
        return launchQuarter;
    }

    public void setLaunchQuarter(String launchQuarter) {
        this.launchQuarter = launchQuarter;
    }

    public boolean isWaitlistEnabled() {
        return waitlistEnabled;
    }

    public void setWaitlistEnabled(boolean waitlistEnabled) {
        this.waitlistEnabled = waitlistEnabled;
    }

    public String getDefaultSubscription() {
        return defaultSubscription;
    }

    public void setDefaultSubscription(String defaultSubscription) {
        this.defaultSubscription = defaultSubscription;
    }
}
