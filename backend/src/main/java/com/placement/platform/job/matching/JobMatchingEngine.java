package com.placement.platform.job.matching;

import com.placement.platform.job.entity.Job;
import com.placement.platform.job.intelligence.CandidateIntelligenceProfile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Stateless matching engine that executes the matching pipeline in memory.
 * It is completely independent of recommendation persistence entities.
 */
@Component
public class JobMatchingEngine {

    private final MatchingPipeline pipeline;

    public JobMatchingEngine(MatchingPipeline pipeline) {
        this.pipeline = pipeline;
    }

    /**
     * Perform all matching in memory.
     */
    public List<RecommendationResult> evaluateJobs(CandidateIntelligenceProfile candidate, List<Job> activeJobs) {
        return activeJobs.stream()
                .map(job -> pipeline.process(candidate, job))
                .collect(Collectors.toList());
    }
}
