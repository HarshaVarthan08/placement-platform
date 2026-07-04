package com.placement.platform.job.matching.strategy;

import com.placement.platform.job.entity.Job;
import com.placement.platform.job.entity.JobSkill;
import com.placement.platform.job.intelligence.CandidateIntelligenceProfile;
import com.placement.platform.job.matching.MatchComponentResult;

import java.util.*;
import java.util.stream.Collectors;

public class SkillMatchStrategy implements MatchStrategy {

    @Override
    public MatchComponentResult evaluate(CandidateIntelligenceProfile candidate, Job job) {
        Set<JobSkill> jobSkills = job.getSkills();
        if (jobSkills == null || jobSkills.isEmpty()) {
            return new MatchComponentResult(
                    "Skills Match",
                    100,
                    100,
                    "No skills required for this job.",
                    new ArrayList<>(),
                    new ArrayList<>()
            );
        }

        // Candidate skills normalized to lowercase for robust match
        Set<String> candidateSkills = candidate.skills().stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .map(String::toLowerCase)
                .collect(Collectors.toSet());

        List<String> matched = new ArrayList<>();
        List<String> missing = new ArrayList<>();

        List<JobSkill> requiredSkills = jobSkills.stream()
                .filter(js -> !js.isPreferred())
                .collect(Collectors.toList());

        List<JobSkill> preferredSkills = jobSkills.stream()
                .filter(JobSkill::isPreferred)
                .collect(Collectors.toList());

        int reqMatched = 0;
        for (JobSkill js : requiredSkills) {
            String skillName = js.getSkillName();
            if (skillName != null) {
                if (candidateSkills.contains(skillName.trim().toLowerCase())) {
                    reqMatched++;
                    matched.add(skillName);
                } else {
                    missing.add(skillName);
                }
            }
        }

        int prefMatched = 0;
        for (JobSkill js : preferredSkills) {
            String skillName = js.getSkillName();
            if (skillName != null) {
                if (candidateSkills.contains(skillName.trim().toLowerCase())) {
                    prefMatched++;
                    matched.add(skillName);
                } else {
                    missing.add(skillName + " (Preferred)");
                }
            }
        }

        double scoreVal = 0.0;
        int reqSize = requiredSkills.size();
        int prefSize = preferredSkills.size();

        if (reqSize > 0 && prefSize > 0) {
            scoreVal = ((double) reqMatched / reqSize) * 80.0 + ((double) prefMatched / prefSize) * 20.0;
        } else if (reqSize > 0) {
            scoreVal = ((double) reqMatched / reqSize) * 100.0;
        } else if (prefSize > 0) {
            scoreVal = ((double) prefMatched / prefSize) * 100.0;
        } else {
            scoreVal = 100.0;
        }

        int score = (int) Math.round(scoreVal);
        String reason = String.format("Matched %d of %d required skills and %d of %d preferred skills.",
                reqMatched, reqSize, prefMatched, prefSize);

        return new MatchComponentResult(
                "Skills Match",
                score,
                100,
                reason,
                matched,
                missing
        );
    }
}
