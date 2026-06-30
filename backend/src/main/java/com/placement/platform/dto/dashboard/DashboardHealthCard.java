package com.placement.platform.dto.dashboard;

import java.time.LocalDateTime;

public record DashboardHealthCard(
    String status,
    HealthMetricDto resumeHealth,
    HealthMetricDto interviewHealth,
    HealthMetricDto profileHealth,
    String overallStatus,
    String overallMessage,
    LocalDateTime lastUpdated
) {}
