package com.placement.platform.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "interview_evaluations")
public class InterviewEvaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interview_session_id", nullable = false, unique = true)
    private InterviewSession interviewSession;

    @Column(name = "overall_score", nullable = false)
    private Integer overallScore;

    @Column(name = "technical_score", nullable = false)
    private Integer technicalScore;

    @Column(name = "communication_score", nullable = false)
    private Integer communicationScore;

    @Column(name = "problem_solving_score", nullable = false)
    private Integer problemSolvingScore;

    @Column(name = "confidence_score", nullable = false)
    private Integer confidenceScore;

    @Column(name = "profile_match_score", nullable = false)
    private Integer profileMatchScore;

    @Enumerated(EnumType.STRING)
    @Column(name = "performance_band", nullable = false, length = 50)
    private PerformanceBand performanceBand;

    @Enumerated(EnumType.STRING)
    @Column(name = "verdict", nullable = false, length = 50)
    private Verdict verdict;

    @Column(name = "verdict_justification", nullable = false, columnDefinition = "TEXT")
    private String verdictJustification;

    @Column(name = "summary", nullable = false, columnDefinition = "TEXT")
    private String summary;

    @Column(name = "overall_feedback", nullable = false, columnDefinition = "TEXT")
    private String overallFeedback;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
        name = "interview_evaluation_strengths",
        joinColumns = @JoinColumn(name = "evaluation_id")
    )
    @Column(name = "strength", columnDefinition = "TEXT")
    private List<String> strengths = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
        name = "interview_evaluation_weaknesses",
        joinColumns = @JoinColumn(name = "evaluation_id")
    )
    @Column(name = "weakness", columnDefinition = "TEXT")
    private List<String> weaknesses = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
        name = "interview_evaluation_recommended_topics",
        joinColumns = @JoinColumn(name = "evaluation_id")
    )
    @Column(name = "recommended_topic", columnDefinition = "TEXT")
    private List<String> recommendedTopics = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
        name = "interview_evaluation_learning_plans",
        joinColumns = @JoinColumn(name = "evaluation_id")
    )
    @Column(name = "learning_plan_item", columnDefinition = "TEXT")
    private List<String> learningPlan = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private EvaluationStatus status = EvaluationStatus.COMPLETED;

    @Column(name = "prompt_version", nullable = false)
    private Integer promptVersion = 1;

    @Column(name = "evaluation_duration_ms", nullable = false)
    private Long evaluationDurationMs;

    @Column(name = "raw_response", columnDefinition = "TEXT")
    private String rawResponse;

    @Column(name = "model_used", nullable = false)
    private String modelUsed;

    @Column(name = "evaluation_version", nullable = false)
    private Integer evaluationVersion = 1;

    @Column(name = "evaluated_at", nullable = false)
    private LocalDateTime evaluatedAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public InterviewEvaluation() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public InterviewSession getInterviewSession() {
        return interviewSession;
    }

    public void setInterviewSession(InterviewSession interviewSession) {
        this.interviewSession = interviewSession;
    }

    public Integer getOverallScore() {
        return overallScore;
    }

    public void setOverallScore(Integer overallScore) {
        this.overallScore = overallScore;
    }

    public Integer getTechnicalScore() {
        return technicalScore;
    }

    public void setTechnicalScore(Integer technicalScore) {
        this.technicalScore = technicalScore;
    }

    public Integer getCommunicationScore() {
        return communicationScore;
    }

    public void setCommunicationScore(Integer communicationScore) {
        this.communicationScore = communicationScore;
    }

    public Integer getProblemSolvingScore() {
        return problemSolvingScore;
    }

    public void setProblemSolvingScore(Integer problemSolvingScore) {
        this.problemSolvingScore = problemSolvingScore;
    }

    public Integer getConfidenceScore() {
        return confidenceScore;
    }

    public void setConfidenceScore(Integer confidenceScore) {
        this.confidenceScore = confidenceScore;
    }

    public Integer getProfileMatchScore() {
        return profileMatchScore;
    }

    public void setProfileMatchScore(Integer profileMatchScore) {
        this.profileMatchScore = profileMatchScore;
    }

    public PerformanceBand getPerformanceBand() {
        return performanceBand;
    }

    public void setPerformanceBand(PerformanceBand performanceBand) {
        this.performanceBand = performanceBand;
    }

    public Verdict getVerdict() {
        return verdict;
    }

    public void setVerdict(Verdict verdict) {
        this.verdict = verdict;
    }

    public String getVerdictJustification() {
        return verdictJustification;
    }

    public void setVerdictJustification(String verdictJustification) {
        this.verdictJustification = verdictJustification;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getOverallFeedback() {
        return overallFeedback;
    }

    public void setOverallFeedback(String overallFeedback) {
        this.overallFeedback = overallFeedback;
    }

    public List<String> getStrengths() {
        return strengths;
    }

    public void setStrengths(List<String> strengths) {
        this.strengths = strengths;
    }

    public List<String> getWeaknesses() {
        return weaknesses;
    }

    public void setWeaknesses(List<String> weaknesses) {
        this.weaknesses = weaknesses;
    }

    public List<String> getRecommendedTopics() {
        return recommendedTopics;
    }

    public void setRecommendedTopics(List<String> recommendedTopics) {
        this.recommendedTopics = recommendedTopics;
    }

    public List<String> getLearningPlan() {
        return learningPlan;
    }

    public void setLearningPlan(List<String> learningPlan) {
        this.learningPlan = learningPlan;
    }

    public EvaluationStatus getStatus() {
        return status;
    }

    public void setStatus(EvaluationStatus status) {
        this.status = status;
    }

    public Integer getPromptVersion() {
        return promptVersion;
    }

    public void setPromptVersion(Integer promptVersion) {
        this.promptVersion = promptVersion;
    }

    public Long getEvaluationDurationMs() {
        return evaluationDurationMs;
    }

    public void setEvaluationDurationMs(Long evaluationDurationMs) {
        this.evaluationDurationMs = evaluationDurationMs;
    }

    public String getRawResponse() {
        return rawResponse;
    }

    public void setRawResponse(String rawResponse) {
        this.rawResponse = rawResponse;
    }

    public String getModelUsed() {
        return modelUsed;
    }

    public void setModelUsed(String modelUsed) {
        this.modelUsed = modelUsed;
    }

    public Integer getEvaluationVersion() {
        return evaluationVersion;
    }

    public void setEvaluationVersion(Integer evaluationVersion) {
        this.evaluationVersion = evaluationVersion;
    }

    public LocalDateTime getEvaluatedAt() {
        return evaluatedAt;
    }

    public void setEvaluatedAt(LocalDateTime evaluatedAt) {
        this.evaluatedAt = evaluatedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
