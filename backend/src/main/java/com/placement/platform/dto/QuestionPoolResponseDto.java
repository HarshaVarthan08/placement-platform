package com.placement.platform.dto;

import com.placement.platform.entity.QuestionPoolStatus;
import java.time.LocalDateTime;
import java.util.List;

public record QuestionPoolResponseDto(
    Long id,
    Integer profileVersion,
    QuestionPoolStatus status,
    LocalDateTime generatedAt,
    LocalDateTime updatedAt,
    List<QuestionResponseDto> questions
) {}
