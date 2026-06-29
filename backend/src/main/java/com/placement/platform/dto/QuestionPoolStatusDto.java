package com.placement.platform.dto;

import java.time.LocalDateTime;

public record QuestionPoolStatusDto(
    String status,
    Integer profileVersion,
    Integer poolVersion,
    LocalDateTime generatedAt
) {}
