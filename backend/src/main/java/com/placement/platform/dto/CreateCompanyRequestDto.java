package com.placement.platform.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Set;

public record CreateCompanyRequestDto(
    @NotBlank(message = "Company name is required")
    @Size(max = 150, message = "Company name must not exceed 150 characters")
    String name,

    String description,

    @DecimalMin(value = "0.0", message = "Min CGPA must be at least 0.0")
    @DecimalMax(value = "10.0", message = "Min CGPA must not exceed 10.0")
    BigDecimal minCgpa,

    Set<String> eligibleBranches,

    Set<String> requiredSkills
) {}
