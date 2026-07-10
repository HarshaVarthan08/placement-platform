package com.placement.platform.job.dto;

import java.time.LocalDateTime;

public class SavedRecommendationResponseDto {
    private Long recommendationId;
    private Long jobId;
    private String company;
    private String title;
    private Integer matchScore;
    private LocalDateTime savedAt;

    public SavedRecommendationResponseDto() {}

    public Long getRecommendationId() { return recommendationId; }
    public void setRecommendationId(Long recommendationId) { this.recommendationId = recommendationId; }

    public Long getJobId() { return jobId; }
    public void setJobId(Long jobId) { this.jobId = jobId; }

    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Integer getMatchScore() { return matchScore; }
    public void setMatchScore(Integer matchScore) { this.matchScore = matchScore; }

    public LocalDateTime getSavedAt() { return savedAt; }
    public void setSavedAt(LocalDateTime savedAt) { this.savedAt = savedAt; }
}
