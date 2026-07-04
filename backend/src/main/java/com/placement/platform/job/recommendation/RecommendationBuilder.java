package com.placement.platform.job.recommendation;

import com.placement.platform.job.matching.RecommendationResult;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class RecommendationBuilder {

    private final RecommendationExplanationBuilder explanationBuilder;

    public RecommendationBuilder(RecommendationExplanationBuilder explanationBuilder) {
        this.explanationBuilder = explanationBuilder;
    }

    public JobRecommendation build(RecommendationResult result, Long userId) {
        JobRecommendation recommendation = new JobRecommendation();
        recommendation.setUserId(userId);
        recommendation.setJobId(result.job().getId());
        recommendation.setMatchScore(result.finalMatchScore());
        recommendation.setConfidenceScore(result.confidenceScore());

        boolean isEligible = result.matchComponentResults().stream()
                .filter(r -> "Eligibility Match".equals(r.componentName()))
                .map(r -> r.score() == 100)
                .findFirst()
                .orElse(true);

        recommendation.setRecommendationLevel(resolveLevel(result.finalMatchScore(), isEligible));
        recommendation.setRecommendationPriority(resolvePriority(result.finalMatchScore(), isEligible));
        recommendation.setRecommendationAction(resolveAction(result.finalMatchScore(), isEligible, result.confidenceScore()));

        recommendation.setMatchedSkills(new ArrayList<>(result.matchedSkills()));
        recommendation.setMissingSkills(new ArrayList<>(result.missingSkills()));
        recommendation.setScoreBreakdown(result.scoreBreakdown());

        // Explanation text
        String reasonText = explanationBuilder.buildExplanation(result);
        recommendation.setRecommendationReason(reasonText);

        // Extra Matching Metrics
        recommendation.setMatchedSkillCount(result.matchedSkillCount());
        recommendation.setTotalRequiredSkills(result.totalRequiredSkills());
        recommendation.setSkillMatchPercentage(result.skillMatchPercentage());

        return recommendation;
    }

    public static RecommendationLevel resolveLevel(int score, boolean isEligible) {
        if (!isEligible) {
            return RecommendationLevel.NOT_RECOMMENDED;
        }
        if (score >= 90) return RecommendationLevel.EXCELLENT;
        if (score >= 75) return RecommendationLevel.STRONG;
        if (score >= 60) return RecommendationLevel.MODERATE;
        return RecommendationLevel.WEAK;
    }

    public static RecommendationPriority resolvePriority(int score, boolean isEligible) {
        if (!isEligible) {
            return RecommendationPriority.NONE;
        }
        if (score >= 75) return RecommendationPriority.HIGH;
        if (score >= 60) return RecommendationPriority.MEDIUM;
        return RecommendationPriority.LOW;
    }

    public static RecommendationAction resolveAction(int score, boolean isEligible) {
        return resolveAction(score, isEligible, 100);
    }

    public static RecommendationAction resolveAction(int score, boolean isEligible, int confidenceScore) {
        if (!isEligible) {
            return RecommendationAction.NOT_ELIGIBLE;
        }
        if (score >= 90) {
            return RecommendationAction.APPLY_NOW;
        }
        if (score >= 75) {
            return RecommendationAction.APPLY_WITH_IMPROVED_RESUME;
        }
        if (score >= 60) {
            return RecommendationAction.IMPROVE_SKILLS;
        }
        if (confidenceScore < 60) {
            return RecommendationAction.COMPLETE_PROFILE;
        }
        return RecommendationAction.SAVE_FOR_LATER;
    }
}
