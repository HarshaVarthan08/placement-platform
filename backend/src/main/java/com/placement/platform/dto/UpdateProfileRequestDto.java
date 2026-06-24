package com.placement.platform.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record UpdateProfileRequestDto(
    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    String name,

    @Size(max = 150, message = "College must not exceed 150 characters")
    String college,

    @Size(max = 100, message = "Degree must not exceed 100 characters")
    String degree,

    @Size(max = 100, message = "Branch must not exceed 100 characters")
    String branch,

    @DecimalMin(value = "0.0", message = "CGPA must be at least 0.0")
    @DecimalMax(value = "10.0", message = "CGPA must not exceed 10.0")
    BigDecimal cgpa,

    @Min(value = 2000, message = "Graduation year must be at least 2000")
    @Max(value = 2100, message = "Graduation year must not exceed 2100")
    Integer graduationYear,

    @Size(max = 100, message = "Target role must not exceed 100 characters")
    String targetRole
) {}
