package com.placement.platform.job.matching.strategy;

import com.placement.platform.job.entity.Job;
import com.placement.platform.job.entity.JobLocation;
import com.placement.platform.job.intelligence.CandidateIntelligenceProfile;
import com.placement.platform.job.matching.MatchComponentResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class LocationMatchStrategy implements MatchStrategy {

    @Override
    public MatchComponentResult evaluate(CandidateIntelligenceProfile candidate, Job job) {
        List<String> preferredLocations = candidate.preferredLocations();
        Set<JobLocation> jobLocations = job.getLocations();

        List<String> matched = new ArrayList<>();
        List<String> missing = new ArrayList<>();

        if (preferredLocations == null || preferredLocations.isEmpty()) {
            matched.add("Open to relocation");
            return new MatchComponentResult(
                    "Location Match",
                    100,
                    100,
                    "No preferred locations specified. Matches job locations by default.",
                    matched,
                    missing
            );
        }

        List<String> jobLocList = jobLocations != null
                ? jobLocations.stream().map(JobLocation::getLocation).filter(Objects::nonNull).collect(Collectors.toList())
                : new ArrayList<>();

        boolean matchedAny = false;
        for (String prefLoc : preferredLocations) {
            if (prefLoc == null || prefLoc.isBlank()) continue;
            String normalizedPref = prefLoc.trim().toLowerCase();

            for (String jobLoc : jobLocList) {
                if (jobLoc != null && jobLoc.trim().toLowerCase().contains(normalizedPref)) {
                    matchedAny = true;
                    matched.add(jobLoc);
                }
            }

            if (!matchedAny) {
                missing.add(prefLoc);
            }
        }

        // Check if job is remote
        boolean isJobRemote = false;
        if (job.getTitle() != null && job.getTitle().toLowerCase().contains("remote")) {
            isJobRemote = true;
        } else if (job.getDescription() != null && job.getDescription().toLowerCase().contains("remote")) {
            isJobRemote = true;
        } else {
            for (String jobLoc : jobLocList) {
                if (jobLoc != null && jobLoc.toLowerCase().contains("remote")) {
                    isJobRemote = true;
                    break;
                }
            }
        }

        if (matchedAny) {
            String reason = String.format("Candidate preferred location matches job location(s): %s",
                    String.join(", ", matched));
            return new MatchComponentResult(
                    "Location Match",
                    100,
                    100,
                    reason,
                    matched,
                    missing
            );
        } else if (isJobRemote) {
            matched.add("Remote work available");
            String reason = "Preferred locations did not match exactly, but the job supports remote work.";
            return new MatchComponentResult(
                    "Location Match",
                    80,
                    100,
                    reason,
                    matched,
                    missing
            );
        } else {
            String reason = String.format("Job locations (%s) do not match preferred locations (%s).",
                    String.join(", ", jobLocList), String.join(", ", preferredLocations));
            return new MatchComponentResult(
                    "Location Match",
                    0,
                    100,
                    reason,
                    matched,
                    missing
            );
        }
    }
}
