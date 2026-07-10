package com.placement.platform.job.dto;

import com.placement.platform.job.recommendation.ApplicationStatus;
import java.time.LocalDateTime;

public class AppliedRecommendationResponseDto {
    private Long recommendationId;
    private Long jobId;
    private String company;
    private String title;
    private Integer matchScore;
    private LocalDateTime appliedAt;
    private ApplicationStatus applicationStatus;
    private String applicationReference;

    public AppliedRecommendationResponseDto() {}

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

    public LocalDateTime getAppliedAt() { return appliedAt; }
    public void setAppliedAt(LocalDateTime appliedAt) { this.appliedAt = appliedAt; }

    public ApplicationStatus getApplicationStatus() { return applicationStatus; }
    public void setApplicationStatus(ApplicationStatus applicationStatus) { this.applicationStatus = applicationStatus; }

    public String getApplicationReference() { return applicationReference; }
    public void setApplicationReference(String applicationReference) { this.applicationReference = applicationReference; }
}
