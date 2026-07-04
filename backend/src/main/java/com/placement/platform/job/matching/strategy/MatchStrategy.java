package com.placement.platform.job.matching.strategy;

import com.placement.platform.job.entity.Job;
import com.placement.platform.job.intelligence.CandidateIntelligenceProfile;
import com.placement.platform.job.matching.MatchComponentResult;

public interface MatchStrategy {
    MatchComponentResult evaluate(CandidateIntelligenceProfile candidate, Job job);
}
