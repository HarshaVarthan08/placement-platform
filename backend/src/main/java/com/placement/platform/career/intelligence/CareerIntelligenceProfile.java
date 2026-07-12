package com.placement.platform.career.intelligence;

import java.time.LocalDateTime;
import java.util.List;

public record CareerIntelligenceProfile(
    Long userId,
    Long recommendationId,
    Long jobId,
    String company,
    String jobTitle,
    int placementReadiness,
    int matchScore,
    int resumeScore,
    int interviewScore,
    int skillScore,
    int careerConfidenceScore,
    CareerConfidenceBand confidenceBand,
    List<String> matchedSkills,
    List<String> missingSkills,
    String highestPrioritySkill,
    String highestPriorityReason,
    CareerInsight highestPriorityInsight,
    List<CareerInsight> insights,
    PreparationDifficulty preparationDifficulty,
    int estimatedPreparationWeeks,
    ProfileHealth profileHealth,
    
    // Future Company Metadata placeholders
    String companyDifficulty,
    String companyCategory,
    Integer estimatedInterviewRounds,
    
    // Metadata
    Integer profileVersion,
    String engineVersion,
    String generatedBy,
    LocalDateTime generatedAt
) {}
