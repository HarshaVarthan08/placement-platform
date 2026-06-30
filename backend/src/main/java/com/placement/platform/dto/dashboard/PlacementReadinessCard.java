package com.placement.platform.dto.dashboard;

import java.time.LocalDateTime;

public record PlacementReadinessCard(
    String status,
    Integer readinessScore,
    String readinessBand,
    ReadinessBreakdownDto breakdown,
    String explanation,
    Integer readinessTrend,
    String deltaPercentage,
    LocalDateTime lastUpdated
) {}
