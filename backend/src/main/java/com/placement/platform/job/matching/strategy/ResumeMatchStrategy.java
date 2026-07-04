package com.placement.platform.job.matching.strategy;

import com.placement.platform.job.entity.Job;
import com.placement.platform.job.intelligence.CandidateIntelligenceProfile;
import com.placement.platform.job.matching.MatchComponentResult;

import java.util.ArrayList;
import java.util.List;

public class ResumeMatchStrategy implements MatchStrategy {

    @Override
    public MatchComponentResult evaluate(CandidateIntelligenceProfile candidate, Job job) {
        Integer atsScore = candidate.resumeATSScore();
        List<String> matched = new ArrayList<>();
        List<String> missing = new ArrayList<>();

        if (atsScore == null || atsScore == 0) {
            missing.add("Resume ATS Score");
            return new MatchComponentResult(
                    "Resume Match",
                    0,
                    100,
                    "Resume has not been uploaded or analyzed.",
                    matched,
                    missing
            );
        }

        matched.add("Resume ATS Score (" + atsScore + ")");
        String reason = String.format("Resume ATS score is %d, matching candidate's analyzed resume.", atsScore);

        return new MatchComponentResult(
                "Resume Match",
                atsScore,
                100,
                reason,
                matched,
                missing
        );
    }
}
