package com.placement.platform.dto;

import java.util.Set;

public record EligibilityResponseDto(
    boolean eligible,
    boolean cgpaMatched,
    boolean branchMatched,
    boolean skillsMatched,
    Set<String> missingSkills
) {}
