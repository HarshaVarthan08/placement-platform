package com.placement.platform.dto.dashboard;

import java.time.LocalDateTime;

public record HealthMetricDto(
    Long ageDays,
    String status,
    String reason,
    LocalDateTime lastUpdated
) {}
