package com.placement.platform.job.dto;

import java.util.List;

public class RecommendationTimelineResponseDto {
    private Long recommendationId;
    private Long jobId;
    private String company;
    private String title;
    private List<RecommendationTimelineEvent> events;

    public RecommendationTimelineResponseDto() {}

    public Long getRecommendationId() { return recommendationId; }
    public void setRecommendationId(Long recommendationId) { this.recommendationId = recommendationId; }

    public Long getJobId() { return jobId; }
    public void setJobId(Long jobId) { this.jobId = jobId; }

    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public List<RecommendationTimelineEvent> getEvents() { return events; }
    public void setEvents(List<RecommendationTimelineEvent> events) { this.events = events; }
}
