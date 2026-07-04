package com.placement.platform.job.matching;

import com.placement.platform.job.intelligence.CandidateIntelligenceProfile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * ScoreAggregator aggregates component match scores using configurable weights
 * and computes the candidate information confidence score.
 *
 * Configured weights in application.properties:
 * - skills = 35%
 * - role = 20%
 * - eligibility = 15%
 * - resume = 10%
 * - experience = 10%
 * - education = 5%
 * - location = 5%
 *
 * Confidence Score Heuristics:
 * - Profile details complete (degree, branch, college, target role, cgpa): +30 points
 * - Resume uploaded and analyzed (ATS score present): +35 points
 * - Interview completed and evaluated (Technical score present): +35 points
 * Total Confidence Score Range: 0 to 100
 */
@Component
public class ScoreAggregator {

    @Value("${application.jobs.matching.weight.skills:35}")
    private int skillsWeight;

    @Value("${application.jobs.matching.weight.role:20}")
    private int roleWeight;

    @Value("${application.jobs.matching.weight.eligibility:15}")
    private int eligibilityWeight;

    @Value("${application.jobs.matching.weight.resume:10}")
    private int resumeWeight;

    @Value("${application.jobs.matching.weight.experience:10}")
    private int experienceWeight;

    @Value("${application.jobs.matching.weight.education:5}")
    private int educationWeight;

    @Value("${application.jobs.matching.weight.location:5}")
    private int locationWeight;

    public ScoreAggregator() {
    }

    // Constructor for testing
    public ScoreAggregator(int skillsWeight, int roleWeight, int eligibilityWeight,
                           int resumeWeight, int experienceWeight, int educationWeight, int locationWeight) {
        this.skillsWeight = skillsWeight;
        this.roleWeight = roleWeight;
        this.eligibilityWeight = eligibilityWeight;
        this.resumeWeight = resumeWeight;
        this.experienceWeight = experienceWeight;
        this.educationWeight = educationWeight;
        this.locationWeight = locationWeight;
    }

    public ScoreBreakdown aggregate(List<MatchComponentResult> results) {
        int skillsScore = 0;
        int roleScore = 0;
        int eligibilityScore = 0;
        int resumeScore = 0;
        int experienceScore = 0;
        int educationScore = 0;
        int locationScore = 0;

        for (MatchComponentResult r : results) {
            String name = r.componentName();
            if (name == null) continue;
            switch (name) {
                case "Skills Match":
                    skillsScore = r.score();
                    break;
                case "Role Match":
                    roleScore = r.score();
                    break;
                case "Eligibility Match":
                    eligibilityScore = r.score();
                    break;
                case "Resume Match":
                    resumeScore = r.score();
                    break;
                case "Experience Match":
                    experienceScore = r.score();
                    break;
                case "Education Match":
                    educationScore = r.score();
                    break;
                case "Location Match":
                    locationScore = r.score();
                    break;
            }
        }

        double weightedSum = (skillsScore * skillsWeight)
                + (roleScore * roleWeight)
                + (eligibilityScore * eligibilityWeight)
                + (resumeScore * resumeWeight)
                + (experienceScore * experienceWeight)
                + (educationScore * educationWeight)
                + (locationScore * locationWeight);

        double totalWeight = skillsWeight + roleWeight + eligibilityWeight
                + resumeWeight + experienceWeight + educationWeight + locationWeight;

        int totalScore = (int) Math.round(weightedSum / (totalWeight > 0 ? totalWeight : 100.0));

        return new ScoreBreakdown(
                skillsScore,
                roleScore,
                eligibilityScore,
                resumeScore,
                experienceScore,
                educationScore,
                locationScore,
                totalScore
        );
    }

    public Integer calculateConfidenceScore(CandidateIntelligenceProfile candidate) {
        int confidence = 0;

        // 1. Profile Completeness (+30)
        boolean hasCgpa = candidate.cgpa() != null;
        boolean hasEdu = candidate.education() != null && !candidate.education().isBlank();
        boolean hasRole = candidate.preferredRoles() != null && !candidate.preferredRoles().isEmpty();
        int profilePoints = 0;
        if (hasCgpa) profilePoints += 10;
        if (hasEdu) profilePoints += 10;
        if (hasRole) profilePoints += 10;
        confidence += profilePoints;

        // 2. Resume Completeness (+35)
        if (candidate.resumeATSScore() != null && candidate.resumeATSScore() > 0) {
            confidence += 35;
        }

        // 3. Interview Completeness (+35)
        if (candidate.interviewTechnicalScore() != null) {
            confidence += 35;
        }

        return confidence;
    }
}
