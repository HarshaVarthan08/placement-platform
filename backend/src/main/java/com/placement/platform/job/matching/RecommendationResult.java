package com.placement.platform.job.matching;

import com.placement.platform.job.entity.Job;
import java.util.List;

/**
 * Capture matching output details before persistence mapping.
 */
public record RecommendationResult(
    Job job,
    Integer finalMatchScore,
    Integer confidenceScore,
    ScoreBreakdown scoreBreakdown,
    List<String> matchedSkills,
    List<String> missingSkills,
    List<MatchComponentResult> matchComponentResults,
    Integer matchedSkillCount,
    Integer totalRequiredSkills,
    Double skillMatchPercentage
) {}
