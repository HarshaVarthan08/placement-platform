package com.placement.platform.job.matching.strategy;

import com.placement.platform.job.entity.Job;
import com.placement.platform.job.intelligence.CandidateIntelligenceProfile;
import com.placement.platform.job.matching.MatchComponentResult;

import java.util.ArrayList;
import java.util.List;

public class EducationMatchStrategy implements MatchStrategy {

    @Override
    public MatchComponentResult evaluate(CandidateIntelligenceProfile candidate, Job job) {
        String education = candidate.education() != null ? candidate.education().toLowerCase() : "";
        String description = job.getDescription() != null ? job.getDescription().toLowerCase() : "";
        String title = job.getTitle() != null ? job.getTitle().toLowerCase() : "";

        List<String> matched = new ArrayList<>();
        List<String> missing = new ArrayList<>();
        int score = 100;

        boolean phdMentioned = description.contains("ph.d") || description.contains("phd") || description.contains("doctorate");
        boolean masterMentioned = description.contains("master") || description.contains("m.tech") || description.contains("ms ") || description.contains("postgraduate");

        boolean hasPhd = education.contains("phd") || education.contains("ph.d") || education.contains("doctor");
        boolean hasMaster = hasPhd || education.contains("master") || education.contains("ms") || education.contains("m.tech") || education.contains("mtech") || education.contains("postgraduate");

        if (phdMentioned && !hasPhd) {
            score -= 30;
            missing.add("Ph.D. / Doctorate");
        } else if (phdMentioned) {
            matched.add("Ph.D. / Doctorate");
        }

        if (masterMentioned && !hasMaster) {
            score -= 15;
            missing.add("Master's / Postgraduate");
        } else if (masterMentioned) {
            matched.add("Master's / Postgraduate");
        }

        // Branch matches job title / description check
        boolean techJob = description.contains("software") || description.contains("developer") || description.contains("engineer") || title.contains("software") || title.contains("developer") || title.contains("engineer");
        boolean csCandidate = education.contains("computer science") || education.contains("cs") || education.contains("information technology") || education.contains("it") || education.contains("software");

        if (techJob && !csCandidate) {
            score -= 15;
            missing.add("CS/IT background preferred for tech roles");
        } else if (techJob) {
            matched.add("Tech background aligns with software role");
        }

        score = Math.max(score, 40); // Cap minimum education score at 40 instead of going lower

        if (missing.isEmpty()) {
            matched.add("Education profile meets all job requirements");
        }

        String reason = missing.isEmpty()
                ? "Candidate education background meets job requirements."
                : "Candidate lacks preferred education credentials: " + String.join(", ", missing);

        return new MatchComponentResult(
                "Education Match",
                score,
                100,
                reason,
                matched,
                missing
        );
    }
}
