package com.placement.platform.dto.dashboard;

import java.time.LocalDateTime;
import java.util.List;

public record SkillAnalyticsCard(
    String status,
    List<SkillPerformanceDto> topSkills,
    List<SkillPerformanceDto> weakestSkills,
    String bestInterviewCategory,
    String lowestInterviewCategory,
    List<String> frequentlyPracticedTopics,
    List<String> frequentlyMissedTopics,
    LocalDateTime lastUpdated
) {}
