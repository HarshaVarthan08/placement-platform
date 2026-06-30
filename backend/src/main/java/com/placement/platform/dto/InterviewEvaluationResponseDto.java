package com.placement.platform.dto;

import java.time.LocalDateTime;
import java.util.List;

public record InterviewEvaluationResponseDto(
    Long id,
    Long interviewSessionId,
    Integer overallScore,
    Integer technicalScore,
    Integer communicationScore,
    Integer problemSolvingScore,
    Integer confidenceScore,
    Integer profileMatchScore,
    String performanceBand,
    String verdict,
    String verdictJustification,
    String summary,
    String overallFeedback,
    List<String> strengths,
    List<String> weaknesses,
    List<String> recommendedTopics,
    List<String> learningPlan,
    String modelUsed,
    String status,
    LocalDateTime evaluatedAt,
    List<QuestionEvaluationResponseDto> questionEvaluations
) {}
