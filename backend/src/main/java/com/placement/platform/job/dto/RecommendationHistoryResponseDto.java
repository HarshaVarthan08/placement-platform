package com.placement.platform.job.dto;

import com.placement.platform.job.recommendation.RecommendationGenerationReason;
import java.time.LocalDateTime;
import java.util.List;

public class RecommendationHistoryResponseDto {
    private String generationId;
    private LocalDateTime generatedAt;
    private RecommendationGenerationReason generationReason;
    private Integer recommendationsCount;
    private List<JobRecommendationResponseDto> recommendations;

    public RecommendationHistoryResponseDto() {}

    public String getGenerationId() { return generationId; }
    public void setGenerationId(String generationId) { this.generationId = generationId; }

    public LocalDateTime getGeneratedAt() { return generatedAt; }
    public void setGeneratedAt(LocalDateTime generatedAt) { this.generatedAt = generatedAt; }

    public RecommendationGenerationReason getGenerationReason() { return generationReason; }
    public void setGenerationReason(RecommendationGenerationReason generationReason) { this.generationReason = generationReason; }

    public Integer getRecommendationsCount() { return recommendationsCount; }
    public void setRecommendationsCount(Integer recommendationsCount) { this.recommendationsCount = recommendationsCount; }

    public List<JobRecommendationResponseDto> getRecommendations() { return recommendations; }
    public void setRecommendations(List<JobRecommendationResponseDto> recommendations) { this.recommendations = recommendations; }
}
