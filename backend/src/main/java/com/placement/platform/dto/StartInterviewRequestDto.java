package com.placement.platform.dto;

import com.placement.platform.entity.InterviewDifficulty;
import com.placement.platform.entity.InterviewMode;
import jakarta.validation.constraints.NotNull;

public record StartInterviewRequestDto(
    @NotNull(message = "Interview mode is required")
    InterviewMode mode,

    InterviewDifficulty difficulty
) {}
