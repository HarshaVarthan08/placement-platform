package com.placement.platform.dto;

import com.placement.platform.entity.AnswerType;
import jakarta.validation.constraints.NotNull;

public record SubmitAnswerRequestDto(
    @NotNull(message = "Answer type is required")
    AnswerType answerType,

    String textAnswer,

    String audioFilePath,

    Integer timeTakenSeconds
) {}
