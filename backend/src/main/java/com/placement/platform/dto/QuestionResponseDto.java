package com.placement.platform.dto;

import com.placement.platform.entity.InterviewDifficulty;
import com.placement.platform.entity.QuestionCategory;
import java.util.List;

public record QuestionResponseDto(
    Long id,
    String question,
    String idealAnswer,
    List<String> keyPoints,
    String topic,
    QuestionCategory category,
    InterviewDifficulty difficulty,
    Integer displayOrder
) {}
