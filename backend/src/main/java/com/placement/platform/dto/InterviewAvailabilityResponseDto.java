package com.placement.platform.dto;

import java.time.LocalDateTime;

public record InterviewAvailabilityResponseDto(
    boolean available,
    LocalDateTime nextAvailableAt,
    long remainingHours,
    long remainingMinutes
) {}
