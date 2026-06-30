package com.placement.platform.dto.dashboard;

public record SkillPerformanceDto(
    String skill,
    Integer score,
    Integer trend,
    String source
) {}
