package com.placement.platform.dto.dashboard;

import java.time.LocalDateTime;

public record ResumeCard(
    String status,
    Integer latestAts,
    Integer resumeVersion,
    Boolean resumeUploaded,
    Boolean resumeAnalysisAvailable,
    LocalDateTime lastResumeUpload,
    LocalDateTime lastResumeAnalysis,
    Integer atsTrend,
    String deltaPercentage,
    LocalDateTime lastUpdated
) {}
