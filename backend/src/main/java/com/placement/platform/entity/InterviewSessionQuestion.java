package com.placement.platform.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "interview_session_questions", indexes = {
    @Index(name = "idx_isq_session_display_order", columnList = "interview_session_id, display_order")
})
public class InterviewSessionQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interview_session_id", nullable = false)
    private InterviewSession interviewSession;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "personalized_question_id")
    private InterviewQuestion personalizedQuestion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "global_question_id")
    private GlobalQuestion globalQuestion;

    @Enumerated(EnumType.STRING)
    @Column(name = "question_source", nullable = false, length = 50)
    private QuestionSource questionSource;

    @Column(name = "display_order", nullable = false)
    private Integer displayOrder;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private SessionQuestionStatus status = SessionQuestionStatus.NOT_STARTED;

    @Enumerated(EnumType.STRING)
    @Column(name = "answer_type", length = 50)
    private AnswerType answerType;

    @Column(name = "text_answer", columnDefinition = "TEXT")
    private String textAnswer;

    @Column(name = "audio_file_path")
    private String audioFilePath;

    @Column(name = "question_started_at")
    private LocalDateTime questionStartedAt;

    @Column(name = "answered_at")
    private LocalDateTime answeredAt;

    @Column(name = "time_taken_seconds")
    private Integer timeTakenSeconds;

    // Snapshot Fields
    @Column(name = "question_text", nullable = false, columnDefinition = "TEXT")
    private String questionText;

    @Column(name = "topic", length = 100)
    private String topic;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", length = 50)
    private QuestionCategory category;

    @Enumerated(EnumType.STRING)
    @Column(name = "difficulty", length = 50)
    private InterviewDifficulty difficulty;

    public InterviewSessionQuestion() {
    }

    @PrePersist
    @PreUpdate
    private void validateFields() {
        boolean hasPersonalized = (personalizedQuestion != null);
        boolean hasGlobal = (globalQuestion != null);
        if (hasPersonalized == hasGlobal) {
            throw new IllegalStateException("Exactly one of personalizedQuestion or globalQuestion must be populated.");
        }
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

    public InterviewQuestion getPersonalizedQuestion() {
        return personalizedQuestion;
    }

    public void setPersonalizedQuestion(InterviewQuestion personalizedQuestion) {
        this.personalizedQuestion = personalizedQuestion;
    }

    public GlobalQuestion getGlobalQuestion() {
        return globalQuestion;
    }

    public void setGlobalQuestion(GlobalQuestion globalQuestion) {
        this.globalQuestion = globalQuestion;
    }

    public QuestionSource getQuestionSource() {
        return questionSource;
    }

    public void setQuestionSource(QuestionSource questionSource) {
        this.questionSource = questionSource;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public SessionQuestionStatus getStatus() {
        return status;
    }

    public void setStatus(SessionQuestionStatus status) {
        this.status = status;
    }

    public AnswerType getAnswerType() {
        return answerType;
    }

    public void setAnswerType(AnswerType answerType) {
        this.answerType = answerType;
    }

    public String getTextAnswer() {
        return textAnswer;
    }

    public void setTextAnswer(String textAnswer) {
        this.textAnswer = textAnswer;
    }

    public String getAudioFilePath() {
        return audioFilePath;
    }

    public void setAudioFilePath(String audioFilePath) {
        this.audioFilePath = audioFilePath;
    }

    public LocalDateTime getQuestionStartedAt() {
        return questionStartedAt;
    }

    public void setQuestionStartedAt(LocalDateTime questionStartedAt) {
        this.questionStartedAt = questionStartedAt;
    }

    public LocalDateTime getAnsweredAt() {
        return answeredAt;
    }

    public void setAnsweredAt(LocalDateTime answeredAt) {
        this.answeredAt = answeredAt;
    }

    public Integer getTimeTakenSeconds() {
        return timeTakenSeconds;
    }

    public void setTimeTakenSeconds(Integer timeTakenSeconds) {
        this.timeTakenSeconds = timeTakenSeconds;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public QuestionCategory getCategory() {
        return category;
    }

    public void setCategory(QuestionCategory category) {
        this.category = category;
    }

    public InterviewDifficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(InterviewDifficulty difficulty) {
        this.difficulty = difficulty;
    }
}
