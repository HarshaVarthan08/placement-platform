package com.placement.platform.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "interview_questions")
public class InterviewQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_pool_id", nullable = false)
    private InterviewQuestionPool questionPool;

    @Column(name = "question", nullable = false, columnDefinition = "TEXT")
    private String question;

    @Column(name = "ideal_answer", columnDefinition = "TEXT")
    private String idealAnswer;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
        name = "interview_question_key_points",
        joinColumns = @JoinColumn(name = "question_id")
    )
    @Column(name = "key_point", columnDefinition = "TEXT")
    private List<String> keyPoints = new ArrayList<>();

    @Column(name = "topic", length = 100)
    private String topic;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", length = 50)
    private QuestionCategory category;

    @Enumerated(EnumType.STRING)
    @Column(name = "difficulty", length = 50)
    private InterviewDifficulty difficulty;

    @Column(name = "display_order")
    private Integer displayOrder;

    public InterviewQuestion() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public InterviewQuestionPool getQuestionPool() {
        return questionPool;
    }

    public void setQuestionPool(InterviewQuestionPool questionPool) {
        this.questionPool = questionPool;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getIdealAnswer() {
        return idealAnswer;
    }

    public void setIdealAnswer(String idealAnswer) {
        this.idealAnswer = idealAnswer;
    }

    public List<String> getKeyPoints() {
        return keyPoints;
    }

    public void setKeyPoints(List<String> keyPoints) {
        this.keyPoints = keyPoints;
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

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }
}
