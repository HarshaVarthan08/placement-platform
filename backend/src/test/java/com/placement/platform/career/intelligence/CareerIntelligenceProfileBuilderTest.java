package com.placement.platform.career.intelligence;

import com.placement.platform.entity.*;
import com.placement.platform.repository.*;
import com.placement.platform.job.recommendation.JobRecommendation;
import com.placement.platform.job.matching.ScoreBreakdown;
import com.placement.platform.job.entity.Job;
import com.placement.platform.career.config.CareerIntelligenceProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CareerIntelligenceProfileBuilderTest {

    @Mock private ResumeRepository resumeRepository;
    @Mock private ResumeAnalysisRepository resumeAnalysisRepository;
    @Mock private InterviewEvaluationRepository interviewEvaluationRepository;
    @Mock private UserSkillRepository userSkillRepository;
    @Mock private UserTargetCompanyRepository userTargetCompanyRepository;
    @Mock private QuestionEvaluationRepository questionEvaluationRepository;
    @Mock private InterviewSessionQuestionRepository interviewSessionQuestionRepository;

    private CareerIntelligenceProfileBuilder builder;
    private CareerIntelligenceProperties properties;
    private User user;
    private JobRecommendation recommendation;
    private Job job;

    @BeforeEach
    void setUp() {
        builder = new CareerIntelligenceProfileBuilder(
                resumeRepository,
                resumeAnalysisRepository,
                interviewEvaluationRepository,
                userSkillRepository,
                userTargetCompanyRepository,
                questionEvaluationRepository,
                interviewSessionQuestionRepository
        );
        properties = new CareerIntelligenceProperties();

        user = new User();
        user.setId(1L);

        recommendation = new JobRecommendation();
        recommendation.setId(22L);
        recommendation.setUserId(1L);
        recommendation.setJobId(10L);
        recommendation.setMatchScore(85);
        recommendation.setReadinessScore(80);
        recommendation.setAtsScore(75);
        recommendation.setMissingSkills(List.of("Docker", "Kubernetes"));
        recommendation.setMatchedSkills(List.of("Java", "Spring Boot"));
        recommendation.setScoreBreakdown(new ScoreBreakdown(80, 90, 85, 75, 70, 80, 90, 85));

        job = new Job();
        job.setId(10L);
        job.setCompany("Google");
        job.setTitle("Software Engineer");
    }

    @Test
    void testBuildProfile_CompleteData() {
        // Mock Resume & Analysis
        Resume resume = new Resume();
        ResumeAnalysis analysis = new ResumeAnalysis();
        analysis.setAtsScore(88);
        analysis.setMissingSkills(List.of("Docker"));
        analysis.setWeaknesses(List.of("AWS"));

        when(resumeRepository.findByUserId(1L)).thenReturn(Optional.of(resume));
        when(resumeAnalysisRepository.findByResume(resume)).thenReturn(Optional.of(analysis));

        // Mock Interview Evaluation
        InterviewEvaluation eval = new InterviewEvaluation();
        eval.setOverallScore(90);
        when(interviewEvaluationRepository.findLatestEvaluationsByUserId(eq(1L), any(Pageable.class)))
                .thenReturn(List.of(eval));

        // Mock Question Evaluations & Topics
        when(questionEvaluationRepository.findAllByUserId(1L)).thenReturn(List.of());
        when(userSkillRepository.findByUserId(1L)).thenReturn(List.of());

        CareerIntelligenceProfile profile = builder.buildProfile(user, recommendation, job, properties);

        assertNotNull(profile);
        assertEquals(1L, profile.userId());
        assertEquals(22L, profile.recommendationId());
        assertEquals(10L, profile.jobId());
        assertEquals("Google", profile.company());
        assertEquals("Software Engineer", profile.jobTitle());
        assertEquals(80, profile.placementReadiness());
        assertEquals(85, profile.matchScore());
        assertEquals(88, profile.resumeScore()); // live score from analysis
        assertEquals(90, profile.interviewScore()); // live score from evaluation
        assertEquals(80, profile.skillScore()); // score from recommendation breakdown
        assertEquals(ProfileHealth.COMPLETE, profile.profileHealth());

        // Derived calculations
        assertEquals(PreparationDifficulty.LOW, profile.preparationDifficulty()); // 2 missing skills <= 3
        assertEquals(2, profile.estimatedPreparationWeeks());

        // Career Insights
        assertFalse(profile.insights().isEmpty());
        assertEquals("Docker", profile.highestPrioritySkill());
        assertEquals("Required by the target role but missing from your profile.", profile.highestPriorityReason());
        assertEquals("VERY_HIGH", profile.highestPriorityInsight().priority());
    }

    @Test
    void testBuildProfile_MissingResumeAndInterview() {
        when(resumeRepository.findByUserId(1L)).thenReturn(Optional.empty());
        when(interviewEvaluationRepository.findLatestEvaluationsByUserId(eq(1L), any(Pageable.class)))
                .thenReturn(List.of());
        when(questionEvaluationRepository.findAllByUserId(1L)).thenReturn(List.of());
        when(userSkillRepository.findByUserId(1L)).thenReturn(List.of());

        CareerIntelligenceProfile profile = builder.buildProfile(user, recommendation, job, properties);

        assertNotNull(profile);
        assertEquals(75, profile.resumeScore()); // Fallback to recommendation ats score snapshot
        assertEquals(0, profile.interviewScore()); // Missing interview evaluation
        assertEquals(ProfileHealth.INCOMPLETE, profile.profileHealth()); // No actual resume analysis or interview evaluation
    }
}
