package com.placement.platform.dto;

import com.placement.platform.entity.InterviewMode;
import com.placement.platform.entity.InterviewStatus;
import java.util.List;

public record InterviewProgressResponseDto(
    Long sessionId,
    InterviewMode mode,
    InterviewStatus status,
    Integer totalQuestions,
    Integer currentQuestionIndex,
    Integer answeredQuestions,
    Integer remainingQuestions,
    List<InterviewQuestionResponseDto> questions
) {}
