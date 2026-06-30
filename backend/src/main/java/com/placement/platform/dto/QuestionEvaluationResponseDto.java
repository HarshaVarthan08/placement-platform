package com.placement.platform.dto;

import java.util.List;

public record QuestionEvaluationResponseDto(
    Long id,
    Long questionId,
    String questionText,
    Integer score,
    List<String> strengths,
    List<String> weaknesses,
    String feedback,
    String improvement,
    String idealAnswer,
    String candidateAnswer
) {}
