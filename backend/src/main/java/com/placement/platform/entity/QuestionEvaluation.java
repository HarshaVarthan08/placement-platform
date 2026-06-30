package com.placement.platform.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "question_evaluations")
public class QuestionEvaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_question_id", nullable = false, unique = true)
    private InterviewSessionQuestion interviewSessionQuestion;

    @Column(name = "score", nullable = false)
    private Integer score;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
        name = "question_evaluation_strengths",
        joinColumns = @JoinColumn(name = "question_evaluation_id")
    )
    @Column(name = "strength", columnDefinition = "TEXT")
    private List<String> strengths = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
        name = "question_evaluation_weaknesses",
        joinColumns = @JoinColumn(name = "question_evaluation_id")
    )
    @Column(name = "weakness", columnDefinition = "TEXT")
    private List<String> weaknesses = new ArrayList<>();

    @Column(name = "feedback", nullable = false, columnDefinition = "TEXT")
    private String feedback;

    @Column(name = "improvement", nullable = false, columnDefinition = "TEXT")
    private String improvement;

    @Column(name = "ideal_answer", nullable = false, columnDefinition = "TEXT")
    private String idealAnswer;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public QuestionEvaluation() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public InterviewSessionQuestion getInterviewSessionQuestion() {
        return interviewSessionQuestion;
    }

    public void setInterviewSessionQuestion(InterviewSessionQuestion interviewSessionQuestion) {
        this.interviewSessionQuestion = interviewSessionQuestion;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
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

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getImprovement() {
        return improvement;
    }

    public void setImprovement(String improvement) {
        this.improvement = improvement;
    }

    public String getIdealAnswer() {
        return idealAnswer;
    }

    public void setIdealAnswer(String idealAnswer) {
        this.idealAnswer = idealAnswer;
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
