package com.placement.platform.job.dto;

import com.placement.platform.job.matching.ScoreBreakdown;
import com.placement.platform.job.recommendation.RecommendationAction;
import com.placement.platform.job.recommendation.RecommendationLevel;
import com.placement.platform.job.recommendation.RecommendationPriority;

import java.time.LocalDateTime;
import java.util.List;

public class JobRecommendationResponseDto {

    private Long id;
    private Long userId;
    private Integer matchScore;
    private Integer confidenceScore;
    private RecommendationLevel recommendationLevel;
    private RecommendationPriority recommendationPriority;
    private RecommendationAction recommendationAction;
    private List<String> matchedSkills;
    private List<String> missingSkills;
    private ScoreBreakdown scoreBreakdown;
    private String recommendationReason;
    private Integer matchedSkillCount;
    private Integer totalRequiredSkills;
    private Double skillMatchPercentage;
    private LocalDateTime generatedAt;
    private LocalDateTime updatedAt;
    private JobResponseDto job;

    public JobRecommendationResponseDto() {
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getMatchScore() {
        return matchScore;
    }

    public void setMatchScore(Integer matchScore) {
        this.matchScore = matchScore;
    }

    public Integer getConfidenceScore() {
        return confidenceScore;
    }

    public void setConfidenceScore(Integer confidenceScore) {
        this.confidenceScore = confidenceScore;
    }

    public RecommendationLevel getRecommendationLevel() {
        return recommendationLevel;
    }

    public void setRecommendationLevel(RecommendationLevel recommendationLevel) {
        this.recommendationLevel = recommendationLevel;
    }

    public RecommendationPriority getRecommendationPriority() {
        return recommendationPriority;
    }

    public void setRecommendationPriority(RecommendationPriority recommendationPriority) {
        this.recommendationPriority = recommendationPriority;
    }

    public RecommendationAction getRecommendationAction() {
        return recommendationAction;
    }

    public void setRecommendationAction(RecommendationAction recommendationAction) {
        this.recommendationAction = recommendationAction;
    }

    public List<String> getMatchedSkills() {
        return matchedSkills;
    }

    public void setMatchedSkills(List<String> matchedSkills) {
        this.matchedSkills = matchedSkills;
    }

    public List<String> getMissingSkills() {
        return missingSkills;
    }

    public void setMissingSkills(List<String> missingSkills) {
        this.missingSkills = missingSkills;
    }

    public ScoreBreakdown getScoreBreakdown() {
        return scoreBreakdown;
    }

    public void setScoreBreakdown(ScoreBreakdown scoreBreakdown) {
        this.scoreBreakdown = scoreBreakdown;
    }

    public String getRecommendationReason() {
        return recommendationReason;
    }

    public void setRecommendationReason(String recommendationReason) {
        this.recommendationReason = recommendationReason;
    }

    public Integer getMatchedSkillCount() {
        return matchedSkillCount;
    }

    public void setMatchedSkillCount(Integer matchedSkillCount) {
        this.matchedSkillCount = matchedSkillCount;
    }

    public Integer getTotalRequiredSkills() {
        return totalRequiredSkills;
    }

    public void setTotalRequiredSkills(Integer totalRequiredSkills) {
        this.totalRequiredSkills = totalRequiredSkills;
    }

    public Double getSkillMatchPercentage() {
        return skillMatchPercentage;
    }

    public void setSkillMatchPercentage(Double skillMatchPercentage) {
        this.skillMatchPercentage = skillMatchPercentage;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public JobResponseDto getJob() {
        return job;
    }

    public void setJob(JobResponseDto job) {
        this.job = job;
    }
}
