package com.placement.platform.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ResumeAnalysisResponseDto(
    Integer atsScore,
    String summary,
    List<String> strengths,
    List<String> weaknesses,
    List<String> missingSkills,
    List<String> suggestions,
    LocalDateTime analyzedAt
) {}
