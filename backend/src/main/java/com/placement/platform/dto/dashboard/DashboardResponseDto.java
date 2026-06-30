package com.placement.platform.dto.dashboard;

import java.time.LocalDateTime;

public record DashboardResponseDto(
    LocalDateTime generatedAt,
    Integer dashboardVersion,
    PlacementReadinessCard placementReadiness,
    ResumeCard resume,
    InterviewCard interview,
    CompanyCard company,
    PracticeCard practice,
    RecommendationCard recommendations,
    DashboardHealthCard health,
    SkillAnalyticsCard skillAnalytics,
    QuickActionCard quickActions
) {}
