package com.placement.platform.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "interview_question_exposures", indexes = {
    @Index(name = "idx_exposure_user_last_practiced", columnList = "user_id, last_practiced_at")
})
public class InterviewQuestionExposure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interview_question_id")
    private InterviewQuestion interviewQuestion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "global_question_id")
    private GlobalQuestion globalQuestion;

    @Enumerated(EnumType.STRING)
    @Column(name = "mode", nullable = false, length = 50)
    private InterviewMode mode;

    @UpdateTimestamp
    @Column(name = "last_practiced_at", nullable = false)
    private LocalDateTime lastPracticedAt;

    @Column(name = "times_shown", nullable = false)
    private Integer timesShown = 0;

    public InterviewQuestionExposure() {
    }

    @PrePersist
    @PreUpdate
    private void validateFields() {
        boolean hasInterviewQuestion = (interviewQuestion != null);
        boolean hasGlobalQuestion = (globalQuestion != null);
        if (hasInterviewQuestion == hasGlobalQuestion) {
            throw new IllegalStateException("Exactly one of interviewQuestion or globalQuestion must be populated.");
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public InterviewQuestion getInterviewQuestion() {
        return interviewQuestion;
    }

    public void setInterviewQuestion(InterviewQuestion interviewQuestion) {
        this.interviewQuestion = interviewQuestion;
    }

    public GlobalQuestion getGlobalQuestion() {
        return globalQuestion;
    }

    public void setGlobalQuestion(GlobalQuestion globalQuestion) {
        this.globalQuestion = globalQuestion;
    }

    public InterviewMode getMode() {
        return mode;
    }

    public void setMode(InterviewMode mode) {
        this.mode = mode;
    }

    public LocalDateTime getLastPracticedAt() {
        return lastPracticedAt;
    }

    public void setLastPracticedAt(LocalDateTime lastPracticedAt) {
        this.lastPracticedAt = lastPracticedAt;
    }

    public Integer getTimesShown() {
        return timesShown;
    }

    public void setTimesShown(Integer timesShown) {
        this.timesShown = timesShown;
    }
}
