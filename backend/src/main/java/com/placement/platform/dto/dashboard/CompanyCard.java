package com.placement.platform.dto.dashboard;

import java.time.LocalDateTime;

public record CompanyCard(
    String status,
    String targetRole,
    Integer eligibleCompanies,
    Integer targetCompanies,
    String placementEligibilitySummary,
    LocalDateTime lastUpdated
) {}
