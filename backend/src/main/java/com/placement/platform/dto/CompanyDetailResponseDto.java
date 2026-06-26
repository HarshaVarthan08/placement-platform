package com.placement.platform.dto;

import java.math.BigDecimal;
import java.util.Set;

public record CompanyDetailResponseDto(
    Long id,
    String name,
    String description,
    BigDecimal minCgpa,
    Set<String> eligibleBranches,
    Set<SkillDto> requiredSkills,
    EligibilityResponseDto eligibility
) {}
