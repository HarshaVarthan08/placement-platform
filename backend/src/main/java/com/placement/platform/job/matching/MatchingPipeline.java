package com.placement.platform.job.matching;

import com.placement.platform.job.entity.Job;
import com.placement.platform.job.entity.JobSkill;
import com.placement.platform.job.intelligence.CandidateIntelligenceProfile;
import com.placement.platform.job.matching.strategy.*;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * MatchingPipeline orchestrates the execution of match strategies in a fixed order,
 * aggregates their scores using ScoreAggregator, and outputs RecommendationResult.
 */
@Component
public class MatchingPipeline {

    private final ScoreAggregator scoreAggregator;
    private final List<MatchStrategy> strategies;

    public MatchingPipeline(ScoreAggregator scoreAggregator) {
        this.scoreAggregator = scoreAggregator;
        // Deterministic fixed order of strategies
        this.strategies = List.of(
                new SkillMatchStrategy(),
                new RoleMatchStrategy(),
                new EligibilityMatchStrategy(),
                new ResumeMatchStrategy(),
                new ExperienceMatchStrategy(),
                new EducationMatchStrategy(),
                new LocationMatchStrategy()
        );
    }

    public RecommendationResult process(CandidateIntelligenceProfile candidate, Job job) {
        List<MatchComponentResult> componentResults = new ArrayList<>();

        // Execute each strategy in the fixed order
        for (MatchStrategy strategy : strategies) {
            componentResults.add(strategy.evaluate(candidate, job));
        }

        // Aggregate scores and breakdown
        ScoreBreakdown breakdown = scoreAggregator.aggregate(componentResults);
        Integer confidenceScore = scoreAggregator.calculateConfidenceScore(candidate);

        // Extract matched and missing skills lists (required & preferred)
        // Find SkillMatchStrategy result (first result in the list)
        MatchComponentResult skillResult = componentResults.get(0);
        List<String> matchedSkills = skillResult.matchedItems();
        List<String> missingSkills = skillResult.missingItems();

        // Calculate skill metrics
        Set<String> candidateSkills = candidate.skills().stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .map(String::toLowerCase)
                .collect(Collectors.toSet());

        List<JobSkill> requiredSkills = job.getSkills().stream()
                .filter(js -> !js.isPreferred())
                .collect(Collectors.toList());

        int totalRequiredSkills = requiredSkills.size();
        int matchedSkillCount = 0;
        for (JobSkill js : requiredSkills) {
            if (js.getSkillName() != null && candidateSkills.contains(js.getSkillName().trim().toLowerCase())) {
                matchedSkillCount++;
            }
        }

        double skillMatchPercentage = totalRequiredSkills > 0
                ? ((double) matchedSkillCount / totalRequiredSkills) * 100.0
                : 100.0;

        return new RecommendationResult(
                job,
                breakdown.totalScore(),
                confidenceScore,
                breakdown,
                matchedSkills,
                missingSkills,
                componentResults,
                matchedSkillCount,
                totalRequiredSkills,
                skillMatchPercentage
        );
    }
}
