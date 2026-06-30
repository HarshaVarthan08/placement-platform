package com.placement.platform.dto.dashboard;

public record RecommendationDto(
    String title,
    String priority,
    Integer priorityScore,
    Integer confidenceScore,
    String reason,
    String suggestedAction,
    String triggeredBy
) {}
