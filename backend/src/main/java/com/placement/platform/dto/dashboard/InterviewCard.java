package com.placement.platform.dto.dashboard;

import java.time.LocalDateTime;

public record InterviewCard(
    String status,
    Integer latestInterviewScore,
    Integer technicalScore,
    Integer communicationScore,
    Integer problemSolvingScore,
    Integer confidenceScore,
    String verdict,
    String performanceBand,
    LocalDateTime lastInterviewDate,
    Integer interviewTrend,
    String deltaPercentage,
    LocalDateTime lastUpdated
) {}
