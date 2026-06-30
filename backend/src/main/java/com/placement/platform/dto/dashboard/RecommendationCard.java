package com.placement.platform.dto.dashboard;

import java.time.LocalDateTime;
import java.util.List;

public record RecommendationCard(
    String status,
    List<RecommendationDto> recommendations,
    LocalDateTime lastUpdated
) {}
