package com.placement.platform.job.dto;

import java.util.List;

public class RecommendationAnalyticsResponseDto {
    private Long totalRecommendations;
    private Long viewedCount;
    private Long savedCount;
    private Long appliedCount;
    private Long hiddenCount;
    private Long archivedCount;
    private Long interviewCount;
    private Long offerCount;
    private Long joinedCount;
    private Long rejectedCount;
    private Double averageMatchScore;
    private Double averageConfidence;
    private List<String> topCompanies;
    private List<String> mostMissingSkills;
    private Double applicationRate;
    private Double saveRate;
    private Double interviewConversionRate;
    private Double offerConversionRate;

    public RecommendationAnalyticsResponseDto() {}

    public Long getTotalRecommendations() { return totalRecommendations; }
    public void setTotalRecommendations(Long totalRecommendations) { this.totalRecommendations = totalRecommendations; }

    public Long getViewedCount() { return viewedCount; }
    public void setViewedCount(Long viewedCount) { this.viewedCount = viewedCount; }

    public Long getSavedCount() { return savedCount; }
    public void setSavedCount(Long savedCount) { this.savedCount = savedCount; }

    public Long getAppliedCount() { return appliedCount; }
    public void setAppliedCount(Long appliedCount) { this.appliedCount = appliedCount; }

    public Long getHiddenCount() { return hiddenCount; }
    public void setHiddenCount(Long hiddenCount) { this.hiddenCount = hiddenCount; }

    public Long getArchivedCount() { return archivedCount; }
    public void setArchivedCount(Long archivedCount) { this.archivedCount = archivedCount; }

    public Long getInterviewCount() { return interviewCount; }
    public void setInterviewCount(Long interviewCount) { this.interviewCount = interviewCount; }

    public Long getOfferCount() { return offerCount; }
    public void setOfferCount(Long offerCount) { this.offerCount = offerCount; }

    public Long getJoinedCount() { return joinedCount; }
    public void setJoinedCount(Long joinedCount) { this.joinedCount = joinedCount; }

    public Long getRejectedCount() { return rejectedCount; }
    public void setRejectedCount(Long rejectedCount) { this.rejectedCount = rejectedCount; }

    public Double getAverageMatchScore() { return averageMatchScore; }
    public void setAverageMatchScore(Double averageMatchScore) { this.averageMatchScore = averageMatchScore; }

    public Double getAverageConfidence() { return averageConfidence; }
    public void setAverageConfidence(Double averageConfidence) { this.averageConfidence = averageConfidence; }

    public List<String> getTopCompanies() { return topCompanies; }
    public void setTopCompanies(List<String> topCompanies) { this.topCompanies = topCompanies; }

    public List<String> getMostMissingSkills() { return mostMissingSkills; }
    public void setMostMissingSkills(List<String> mostMissingSkills) { this.mostMissingSkills = mostMissingSkills; }

    public Double getApplicationRate() { return applicationRate; }
    public void setApplicationRate(Double applicationRate) { this.applicationRate = applicationRate; }

    public Double getSaveRate() { return saveRate; }
    public void setSaveRate(Double saveRate) { this.saveRate = saveRate; }

    public Double getInterviewConversionRate() { return interviewConversionRate; }
    public void setInterviewConversionRate(Double interviewConversionRate) { this.interviewConversionRate = interviewConversionRate; }

    public Double getOfferConversionRate() { return offerConversionRate; }
    public void setOfferConversionRate(Double offerConversionRate) { this.offerConversionRate = offerConversionRate; }
}
