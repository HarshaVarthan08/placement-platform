package com.placement.platform.job.dto;

import com.placement.platform.job.matching.ScoreBreakdown;
import com.placement.platform.job.recommendation.*;

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

    // Lifecycle states
    private RecommendationStatus recommendationStatus;
    private LocalDateTime viewedAt;
    private LocalDateTime savedAt;
    private LocalDateTime appliedAt;
    private LocalDateTime archivedAt;
    private LocalDateTime hiddenAt;
    private Integer viewCount;
    private Integer recommendationVersion;
    private String generationId;
    private RecommendationGenerationReason generationReason;
    private LocalDateTime lastRefreshedAt;
    private Boolean hidden;

    // Snapshot fields
    private Integer atsScore;
    private Integer readinessScore;
    private String resumeVersion;

    // Application Tracking
    private ApplicationStatus applicationStatus;
    private String applicationUrl;
    private String applicationReference;
    private String applicationNotes;

    // Feedback
    private RecommendationFeedbackType feedbackType;
    private String feedbackNotes;

    public JobRecommendationResponseDto() {
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Integer getMatchScore() { return matchScore; }
    public void setMatchScore(Integer matchScore) { this.matchScore = matchScore; }

    public Integer getConfidenceScore() { return confidenceScore; }
    public void setConfidenceScore(Integer confidenceScore) { this.confidenceScore = confidenceScore; }

    public RecommendationLevel getRecommendationLevel() { return recommendationLevel; }
    public void setRecommendationLevel(RecommendationLevel recommendationLevel) { this.recommendationLevel = recommendationLevel; }

    public RecommendationPriority getRecommendationPriority() { return recommendationPriority; }
    public void setRecommendationPriority(RecommendationPriority recommendationPriority) { this.recommendationPriority = recommendationPriority; }

    public RecommendationAction getRecommendationAction() { return recommendationAction; }
    public void setRecommendationAction(RecommendationAction recommendationAction) { this.recommendationAction = recommendationAction; }

    public List<String> getMatchedSkills() { return matchedSkills; }
    public void setMatchedSkills(List<String> matchedSkills) { this.matchedSkills = matchedSkills; }

    public List<String> getMissingSkills() { return missingSkills; }
    public void setMissingSkills(List<String> missingSkills) { this.missingSkills = missingSkills; }

    public ScoreBreakdown getScoreBreakdown() { return scoreBreakdown; }
    public void setScoreBreakdown(ScoreBreakdown scoreBreakdown) { this.scoreBreakdown = scoreBreakdown; }

    public String getRecommendationReason() { return recommendationReason; }
    public void setRecommendationReason(String recommendationReason) { this.recommendationReason = recommendationReason; }

    public Integer getMatchedSkillCount() { return matchedSkillCount; }
    public void setMatchedSkillCount(Integer matchedSkillCount) { this.matchedSkillCount = matchedSkillCount; }

    public Integer getTotalRequiredSkills() { return totalRequiredSkills; }
    public void setTotalRequiredSkills(Integer totalRequiredSkills) { this.totalRequiredSkills = totalRequiredSkills; }

    public Double getSkillMatchPercentage() { return skillMatchPercentage; }
    public void setSkillMatchPercentage(Double skillMatchPercentage) { this.skillMatchPercentage = skillMatchPercentage; }

    public LocalDateTime getGeneratedAt() { return generatedAt; }
    public void setGeneratedAt(LocalDateTime generatedAt) { this.generatedAt = generatedAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public JobResponseDto getJob() { return job; }
    public void setJob(JobResponseDto job) { this.job = job; }

    public RecommendationStatus getRecommendationStatus() { return recommendationStatus; }
    public void setRecommendationStatus(RecommendationStatus recommendationStatus) { this.recommendationStatus = recommendationStatus; }

    public LocalDateTime getViewedAt() { return viewedAt; }
    public void setViewedAt(LocalDateTime viewedAt) { this.viewedAt = viewedAt; }

    public LocalDateTime getSavedAt() { return savedAt; }
    public void setSavedAt(LocalDateTime savedAt) { this.savedAt = savedAt; }

    public LocalDateTime getAppliedAt() { return appliedAt; }
    public void setAppliedAt(LocalDateTime appliedAt) { this.appliedAt = appliedAt; }

    public LocalDateTime getArchivedAt() { return archivedAt; }
    public void setArchivedAt(LocalDateTime archivedAt) { this.archivedAt = archivedAt; }

    public LocalDateTime getHiddenAt() { return hiddenAt; }
    public void setHiddenAt(LocalDateTime hiddenAt) { this.hiddenAt = hiddenAt; }

    public Integer getViewCount() { return viewCount; }
    public void setViewCount(Integer viewCount) { this.viewCount = viewCount; }

    public Integer getRecommendationVersion() { return recommendationVersion; }
    public void setRecommendationVersion(Integer recommendationVersion) { this.recommendationVersion = recommendationVersion; }

    public String getGenerationId() { return generationId; }
    public void setGenerationId(String generationId) { this.generationId = generationId; }

    public RecommendationGenerationReason getGenerationReason() { return generationReason; }
    public void setGenerationReason(RecommendationGenerationReason generationReason) { this.generationReason = generationReason; }

    public LocalDateTime getLastRefreshedAt() { return lastRefreshedAt; }
    public void setLastRefreshedAt(LocalDateTime lastRefreshedAt) { this.lastRefreshedAt = lastRefreshedAt; }

    public Boolean getHidden() { return hidden; }
    public void setHidden(Boolean hidden) { this.hidden = hidden; }

    public Integer getAtsScore() { return atsScore; }
    public void setAtsScore(Integer atsScore) { this.atsScore = atsScore; }

    public Integer getReadinessScore() { return readinessScore; }
    public void setReadinessScore(Integer readinessScore) { this.readinessScore = readinessScore; }

    public String getResumeVersion() { return resumeVersion; }
    public void setResumeVersion(String resumeVersion) { this.resumeVersion = resumeVersion; }

    public ApplicationStatus getApplicationStatus() { return applicationStatus; }
    public void setApplicationStatus(ApplicationStatus applicationStatus) { this.applicationStatus = applicationStatus; }

    public String getApplicationUrl() { return applicationUrl; }
    public void setApplicationUrl(String applicationUrl) { this.applicationUrl = applicationUrl; }

    public String getApplicationReference() { return applicationReference; }
    public void setApplicationReference(String applicationReference) { this.applicationReference = applicationReference; }

    public String getApplicationNotes() { return applicationNotes; }
    public void setApplicationNotes(String applicationNotes) { this.applicationNotes = applicationNotes; }

    public RecommendationFeedbackType getFeedbackType() { return feedbackType; }
    public void setFeedbackType(RecommendationFeedbackType feedbackType) { this.feedbackType = feedbackType; }

    public String getFeedbackNotes() { return feedbackNotes; }
    public void setFeedbackNotes(String feedbackNotes) { this.feedbackNotes = feedbackNotes; }
}
