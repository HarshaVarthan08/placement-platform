package com.placement.platform.career.intelligence;

import com.placement.platform.entity.User;
import com.placement.platform.job.recommendation.JobRecommendation;
import com.placement.platform.job.entity.Job;
import com.placement.platform.career.config.CareerIntelligenceProperties;
import org.springframework.stereotype.Service;

@Service
public class CareerIntelligenceEngine {

    private final CareerIntelligenceProfileBuilder profileBuilder;
    private final CareerIntelligenceProperties properties;

    public CareerIntelligenceEngine(
            CareerIntelligenceProfileBuilder profileBuilder,
            CareerIntelligenceProperties properties
    ) {
        this.profileBuilder = profileBuilder;
        this.properties = properties;
    }

    public CareerIntelligenceProfile buildProfile(User user, JobRecommendation recommendation, Job job) {
        return profileBuilder.buildProfile(user, recommendation, job, properties);
    }
}
