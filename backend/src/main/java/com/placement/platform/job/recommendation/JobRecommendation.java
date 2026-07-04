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
    uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "job_id"})
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
}
