package com.placement.platform.dto;

import java.math.BigDecimal;

public record ProfileResponseDto(
    Long id,
    String name,
    String email,
    String college,
    String degree,
    String branch,
    BigDecimal cgpa,
    Integer graduationYear,
    String targetRole
) {}
