package com.placement.platform.job.matching;

/**
 * Strongly typed DTO representing the score breakdown for all strategies.
 */
public record ScoreBreakdown(
    Integer skillScore,
    Integer roleScore,
    Integer eligibilityScore,
    Integer resumeScore,
    Integer experienceScore,
    Integer educationScore,
    Integer locationScore,
    Integer totalScore
) {}
