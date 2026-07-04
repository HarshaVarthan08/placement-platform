package com.placement.platform.job.dto;

public class RecommendationSummaryDto {

    private long totalRecommendations;
    private long excellentMatches;
    private long strongMatches;
    private double averageMatchScore;
    private double averageConfidence;
    private String topRecommendedCompany;

    public RecommendationSummaryDto() {
    }

    public RecommendationSummaryDto(long totalRecommendations, long excellentMatches, long strongMatches,
                                     double averageMatchScore, double averageConfidence, String topRecommendedCompany) {
        this.totalRecommendations = totalRecommendations;
        this.excellentMatches = excellentMatches;
        this.strongMatches = strongMatches;
        this.averageMatchScore = averageMatchScore;
        this.averageConfidence = averageConfidence;
        this.topRecommendedCompany = topRecommendedCompany;
    }

    public long getTotalRecommendations() {
        return totalRecommendations;
    }

    public void setTotalRecommendations(long totalRecommendations) {
        this.totalRecommendations = totalRecommendations;
    }

    public long getExcellentMatches() {
        return excellentMatches;
    }

    public void setExcellentMatches(long excellentMatches) {
        this.excellentMatches = excellentMatches;
    }

    public long getStrongMatches() {
        return strongMatches;
    }

    public void setStrongMatches(long strongMatches) {
        this.strongMatches = strongMatches;
    }

    public double getAverageMatchScore() {
        return averageMatchScore;
    }

    public void setAverageMatchScore(double averageMatchScore) {
        this.averageMatchScore = averageMatchScore;
    }

    public double getAverageConfidence() {
        return averageConfidence;
    }

    public void setAverageConfidence(double averageConfidence) {
        this.averageConfidence = averageConfidence;
    }

    public String getTopRecommendedCompany() {
        return topRecommendedCompany;
    }

    public void setTopRecommendedCompany(String topRecommendedCompany) {
        this.topRecommendedCompany = topRecommendedCompany;
    }
}
