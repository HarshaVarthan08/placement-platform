package com.placement.platform.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "global_questions")
public class GlobalQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "question", nullable = false, columnDefinition = "TEXT")
    private String question;

    @Column(name = "ideal_answer", columnDefinition = "TEXT")
    private String idealAnswer;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
        name = "global_question_key_points",
        joinColumns = @JoinColumn(name = "question_id")
    )
    @Column(name = "key_point", columnDefinition = "TEXT")
    private List<String> keyPoints = new ArrayList<>();

    @Column(name = "topic", length = 100)
    private String topic;

    @Enumerated(EnumType.STRING)
    @Column(name = "difficulty", length = 50)
    private InterviewDifficulty difficulty;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
        name = "global_question_roles",
        joinColumns = @JoinColumn(name = "question_id")
    )
    @Column(name = "role")
    private List<String> applicableRoles = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
        name = "global_question_skills",
        joinColumns = @JoinColumn(name = "question_id")
    )
    @Column(name = "skill_tag")
    private List<String> skillTags = new ArrayList<>();

    public GlobalQuestion() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public InterviewDifficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(InterviewDifficulty difficulty) {
        this.difficulty = difficulty;
    }

    public List<String> getApplicableRoles() {
        return applicableRoles;
    }

    public void setApplicableRoles(List<String> applicableRoles) {
        this.applicableRoles = applicableRoles;
    }

    public List<String> getSkillTags() {
        return skillTags;
    }

    public void setSkillTags(List<String> skillTags) {
        this.skillTags = skillTags;
    }
}
