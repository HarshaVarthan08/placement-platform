package com.placement.platform.dto;

import com.placement.platform.entity.InterviewMode;
import com.placement.platform.entity.InterviewStatus;
import java.time.LocalDateTime;

public record InterviewSessionResponseDto(
    Long sessionId,
    InterviewMode mode,
    InterviewStatus status,
    LocalDateTime startedAt
) {}
