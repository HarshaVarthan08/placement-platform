package com.placement.platform.dto;

import com.placement.platform.entity.InterviewDifficulty;
import com.placement.platform.entity.QuestionCategory;
import com.placement.platform.entity.QuestionSource;
import com.placement.platform.entity.SessionQuestionStatus;

public record InterviewQuestionResponseDto(
    Long sessionQuestionId,
    String question,
    String topic,
    InterviewDifficulty difficulty,
    QuestionCategory category,
    Integer displayOrder,
    QuestionSource questionSource,
    SessionQuestionStatus status
) {}
