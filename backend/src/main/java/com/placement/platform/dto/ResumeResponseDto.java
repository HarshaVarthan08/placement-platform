package com.placement.platform.dto;

import java.time.LocalDateTime;

public record ResumeResponseDto(
    Long id,
    String originalFileName,
    String fileType,
    Long fileSize,
    LocalDateTime uploadedAt,
    LocalDateTime updatedAt
) {}
