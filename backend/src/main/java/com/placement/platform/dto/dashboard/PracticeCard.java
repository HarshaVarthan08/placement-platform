package com.placement.platform.dto.dashboard;

import java.time.LocalDateTime;

public record PracticeCard(
    String status,
    Long learningInterviews,
    Long mockInterviews,
    Long totalInterviews,
    Long learningQuestionsAttempted,
    Long mockQuestionsAttempted,
    LocalDateTime lastUpdated
) {}
