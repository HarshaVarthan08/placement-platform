package com.placement.platform.job.recommendation;

import com.placement.platform.job.matching.ScoreBreakdown;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
    name = "job_recommendations",
    uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "job_id", "generation_id"})
)
public class JobRecommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "job_id", nullable = false)
    private Long jobId;

    @Column(name = "match_score", nullable = false)
    private Integer matchScore;

    @Column(name = "confidence_score", nullable = false)
    private Integer confidenceScore;

    @Enumerated(EnumType.STRING)
    @Column(name = "recommendation_level", nullable = false, length = 50)
    private RecommendationLevel recommendationLevel;

    @Enumerated(EnumType.STRING)
    @Column(name = "recommendation_priority", nullable = false, length = 50)
    private RecommendationPriority recommendationPriority;

    @Enumerated(EnumType.STRING)
    @Column(name = "recommendation_action", nullable = false, length = 50)
    private RecommendationAction recommendationAction;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
        name = "job_recommendation_matched_skills",
        joinColumns = @JoinColumn(name = "recommendation_id")
    )
    @Column(name = "skill_name")
    private List<String> matchedSkills = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
        name = "job_recommendation_missing_skills",
        joinColumns = @JoinColumn(name = "recommendation_id")
    )
    @Column(name = "skill_name")
    private List<String> missingSkills = new ArrayList<>();

    @Column(name = "score_breakdown", columnDefinition = "TEXT")
    @Convert(converter = ScoreBreakdownConverter.class)
    private ScoreBreakdown scoreBreakdown;

    @Column(name = "recommendation_reason", columnDefinition = "TEXT")
    private String recommendationReason;

    // Matching Metrics
    @Column(name = "matched_skill_count", nullable = false)
    private Integer matchedSkillCount = 0;

    @Column(name = "total_required_skills", nullable = false)
    private Integer totalRequiredSkills = 0;

    @Column(name = "skill_match_percentage", nullable = false)
    private Double skillMatchPercentage = 0.0;

    @CreationTimestamp
    @Column(name = "generated_at", nullable = false, updatable = false)
    private LocalDateTime generatedAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "recommendation_status", nullable = false, length = 50)
    private RecommendationStatus recommendationStatus = RecommendationStatus.NEW;

    @Column(name = "viewed_at")
    private LocalDateTime viewedAt;

    @Column(name = "saved_at")
    private LocalDateTime savedAt;

    @Column(name = "applied_at")
    private LocalDateTime appliedAt;

    @Column(name = "archived_at")
    private LocalDateTime archivedAt;

    @Column(name = "hidden_at")
    private LocalDateTime hiddenAt;

    @Column(name = "view_count", nullable = false)
    private Integer viewCount = 0;

    @Column(name = "recommendation_version", nullable = false)
    private Integer recommendationVersion = 1;

    @Column(name = "generation_id", length = 100)
    private String generationId;

    @Enumerated(EnumType.STRING)
    @Column(name = "generation_reason", nullable = false, length = 50)
    private RecommendationGenerationReason generationReason = RecommendationGenerationReason.MANUAL;

    @Column(name = "last_refreshed_at")
    private LocalDateTime lastRefreshedAt;

    @Column(name = "hidden", nullable = false)
    private Boolean hidden = false;

    // Snapshot fields
    @Column(name = "ats_score")
    private Integer atsScore;

    @Column(name = "readiness_score")
    private Integer readinessScore;

    @Column(name = "resume_version", length = 100)
    private String resumeVersion;

    // Application Tracking
    @Enumerated(EnumType.STRING)
    @Column(name = "application_status", length = 50)
    private ApplicationStatus applicationStatus;

    @Column(name = "application_url", length = 500)
    private String applicationUrl;

    @Column(name = "application_reference", length = 100)
    private String applicationReference;

    @Column(name = "application_notes", columnDefinition = "TEXT")
    private String applicationNotes;

    // Feedback
    @Enumerated(EnumType.STRING)
    @Column(name = "feedback_type", length = 50)
    private RecommendationFeedbackType feedbackType;

    @Column(name = "feedback_notes", columnDefinition = "TEXT")
    private String feedbackNotes;

    public JobRecommendation() {
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

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
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

    // Getters and Setters for new fields
    public RecommendationStatus getRecommendationStatus() {
        return recommendationStatus != null ? recommendationStatus : RecommendationStatus.NEW;
    }

    public void setRecommendationStatus(RecommendationStatus recommendationStatus) {
        this.recommendationStatus = recommendationStatus;
    }

    public LocalDateTime getViewedAt() {
        return viewedAt;
    }

    public void setViewedAt(LocalDateTime viewedAt) {
        this.viewedAt = viewedAt;
    }

    public LocalDateTime getSavedAt() {
        return savedAt;
    }

    public void setSavedAt(LocalDateTime savedAt) {
        this.savedAt = savedAt;
    }

    public LocalDateTime getAppliedAt() {
        return appliedAt;
    }

    public void setAppliedAt(LocalDateTime appliedAt) {
        this.appliedAt = appliedAt;
    }

    public LocalDateTime getArchivedAt() {
        return archivedAt;
    }

    public void setArchivedAt(LocalDateTime archivedAt) {
        this.archivedAt = archivedAt;
    }

    public LocalDateTime getHiddenAt() {
        return hiddenAt;
    }

    public void setHiddenAt(LocalDateTime hiddenAt) {
        this.hiddenAt = hiddenAt;
    }

    public Integer getViewCount() {
        return viewCount != null ? viewCount : 0;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public Integer getRecommendationVersion() {
        return recommendationVersion != null ? recommendationVersion : 1;
    }

    public void setRecommendationVersion(Integer recommendationVersion) {
        this.recommendationVersion = recommendationVersion;
    }

    public String getGenerationId() {
        return generationId;
    }

    public void setGenerationId(String generationId) {
        this.generationId = generationId;
    }

    public RecommendationGenerationReason getGenerationReason() {
        return generationReason != null ? generationReason : RecommendationGenerationReason.MANUAL;
    }

    public void setGenerationReason(RecommendationGenerationReason generationReason) {
        this.generationReason = generationReason;
    }

    public LocalDateTime getLastRefreshedAt() {
        return lastRefreshedAt;
    }

    public void setLastRefreshedAt(LocalDateTime lastRefreshedAt) {
        this.lastRefreshedAt = lastRefreshedAt;
    }

    public Boolean getHidden() {
        return hidden != null ? hidden : false;
    }

    public void setHidden(Boolean hidden) {
        this.hidden = hidden;
    }

    public Integer getAtsScore() {
        return atsScore;
    }

    public void setAtsScore(Integer atsScore) {
        this.atsScore = atsScore;
    }

    public Integer getReadinessScore() {
        return readinessScore;
    }

    public void setReadinessScore(Integer readinessScore) {
        this.readinessScore = readinessScore;
    }

    public String getResumeVersion() {
        return resumeVersion;
    }

    public void setResumeVersion(String resumeVersion) {
        this.resumeVersion = resumeVersion;
    }

    public ApplicationStatus getApplicationStatus() {
        return applicationStatus;
    }

    public void setApplicationStatus(ApplicationStatus applicationStatus) {
        this.applicationStatus = applicationStatus;
    }

    public String getApplicationUrl() {
        return applicationUrl;
    }

    public void setApplicationUrl(String applicationUrl) {
        this.applicationUrl = applicationUrl;
    }

    public String getApplicationReference() {
        return applicationReference;
    }

    public void setApplicationReference(String applicationReference) {
        this.applicationReference = applicationReference;
    }

    public String getApplicationNotes() {
        return applicationNotes;
    }

    public void setApplicationNotes(String applicationNotes) {
        this.applicationNotes = applicationNotes;
    }

    public RecommendationFeedbackType getFeedbackType() {
        return feedbackType;
    }

    public void setFeedbackType(RecommendationFeedbackType feedbackType) {
        this.feedbackType = feedbackType;
    }

    public String getFeedbackNotes() {
        return feedbackNotes;
    }

    public void setFeedbackNotes(String feedbackNotes) {
        this.feedbackNotes = feedbackNotes;
    }
}
