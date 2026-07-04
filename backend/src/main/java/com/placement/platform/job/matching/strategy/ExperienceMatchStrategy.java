package com.placement.platform.job.matching.strategy;

import com.placement.platform.job.entity.Job;
import com.placement.platform.job.intelligence.CandidateIntelligenceProfile;
import com.placement.platform.job.matching.MatchComponentResult;

import java.util.ArrayList;
import java.util.List;

public class ExperienceMatchStrategy implements MatchStrategy {

    @Override
    public MatchComponentResult evaluate(CandidateIntelligenceProfile candidate, Job job) {
        Integer requiredExp = job.getMinimumExperience();
        Integer candidateExp = candidate.experience();
        if (candidateExp == null) {
            candidateExp = 0;
        }

        List<String> matched = new ArrayList<>();
        List<String> missing = new ArrayList<>();

        if (requiredExp == null || requiredExp == 0) {
            matched.add("No experience required");
            return new MatchComponentResult(
                    "Experience Match",
                    100,
                    100,
                    "No experience required for this job.",
                    matched,
                    missing
            );
        }

        if (candidateExp >= requiredExp) {
            matched.add("Required experience: " + requiredExp + " years (Candidate has: " + candidateExp + " years)");
            String reason = String.format("Candidate meets the minimum experience requirement of %d years.", requiredExp);
            return new MatchComponentResult(
                    "Experience Match",
                    100,
                    100,
                    reason,
                    matched,
                    missing
            );
        } else {
            missing.add("Required experience: " + requiredExp + " years (Candidate has: " + candidateExp + " years)");
            int score = (int) Math.round(((double) candidateExp / requiredExp) * 100.0);
            String reason = String.format("Candidate has %d years of experience, which is below the required %d years.",
                    candidateExp, requiredExp);
            return new MatchComponentResult(
                    "Experience Match",
                    score,
                    100,
                    reason,
                    matched,
                    missing
            );
        }
    }
}
