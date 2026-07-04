package com.placement.platform.job.matching.strategy;

import com.placement.platform.job.entity.Job;
import com.placement.platform.job.intelligence.CandidateIntelligenceProfile;
import com.placement.platform.job.matching.MatchComponentResult;

import java.util.ArrayList;
import java.util.List;

public class RoleMatchStrategy implements MatchStrategy {

    @Override
    public MatchComponentResult evaluate(CandidateIntelligenceProfile candidate, Job job) {
        List<String> preferredRoles = candidate.preferredRoles();
        if (preferredRoles == null || preferredRoles.isEmpty()) {
            return new MatchComponentResult(
                    "Role Match",
                    80,
                    100,
                    "No preferred roles specified. General match applied.",
                    new ArrayList<>(),
                    new ArrayList<>()
            );
        }

        String jobRole = job.getNormalizedRole() != null ? job.getNormalizedRole().trim().toLowerCase() : "";
        String jobTitle = job.getTitle() != null ? job.getTitle().trim().toLowerCase() : "";

        int maxScore = 0;
        String bestReason = "Role did not match preferred roles.";
        List<String> matchedRoles = new ArrayList<>();
        List<String> missingRoles = new ArrayList<>();

        for (String prefRole : preferredRoles) {
            if (prefRole == null || prefRole.isBlank()) continue;
            String normalizedPref = prefRole.trim().toLowerCase();

            if (jobRole.equals(normalizedPref)) {
                maxScore = Math.max(maxScore, 100);
                matchedRoles.add(prefRole);
            } else if (jobTitle.contains(normalizedPref) || normalizedPref.contains(jobTitle)) {
                maxScore = Math.max(maxScore, 85);
                matchedRoles.add(prefRole);
            } else if (job.getDescription() != null && job.getDescription().toLowerCase().contains(normalizedPref)) {
                maxScore = Math.max(maxScore, 60);
                matchedRoles.add(prefRole);
            } else {
                missingRoles.add(prefRole);
            }
        }

        if (maxScore == 100) {
            bestReason = "Normalized role matched exactly: " + String.join(", ", matchedRoles);
        } else if (maxScore == 85) {
            bestReason = "Preferred role matches job title: " + String.join(", ", matchedRoles);
        } else if (maxScore == 60) {
            bestReason = "Preferred role found in job description: " + String.join(", ", matchedRoles);
        } else {
            maxScore = 30; // Mismatch baseline
            bestReason = "Job role/title does not match preferred roles: " + String.join(", ", preferredRoles);
        }

        return new MatchComponentResult(
                "Role Match",
                maxScore,
                100,
                bestReason,
                matchedRoles,
                missingRoles
        );
    }
}
