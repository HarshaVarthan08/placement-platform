package com.placement.platform.service;

import com.placement.platform.dto.EligibilityResponseDto;
import com.placement.platform.dto.dashboard.*;
import com.placement.platform.entity.*;
import com.placement.platform.repository.*;
import com.placement.platform.service.EligibilityService;
import com.placement.platform.service.dashboard.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PlacementIntelligenceServiceTest {

    @Mock
    private ResumeRepository resumeRepository;
    @Mock
    private ResumeAnalysisRepository resumeAnalysisRepository;
    @Mock
    private InterviewEvaluationRepository interviewEvaluationRepository;
    @Mock
    private InterviewSessionRepository interviewSessionRepository;
    @Mock
    private InterviewSessionQuestionRepository interviewSessionQuestionRepository;
    @Mock
    private QuestionEvaluationRepository questionEvaluationRepository;
    @Mock
    private UserTargetCompanyRepository userTargetCompanyRepository;
    @Mock
    private TargetCompanyRepository targetCompanyRepository;
    @Mock
    private UserSkillRepository userSkillRepository;
    @Mock
    private EligibilityService eligibilityService;

    @InjectMocks
    private PlacementDataAggregator dataAggregator;

    @InjectMocks
    private ReadinessServiceImpl readinessService;

    @InjectMocks
    private RecommendationEngineImpl recommendationEngine;

    @InjectMocks
    private DashboardHealthServiceImpl dashboardHealthService;

    @InjectMocks
    private SkillAnalyticsServiceImpl skillAnalyticsService;

    private DashboardAnalyticsServiceImpl dashboardAnalyticsService;
    private PlacementIntelligenceServiceImpl placementIntelligenceService;

    private User student;

    @BeforeEach
    void setUp() {
        // Setup configuration properties for ReadinessService via reflection
        ReflectionTestUtils.setField(readinessService, "resumeWeight", 30);
        ReflectionTestUtils.setField(readinessService, "interviewWeight", 35);
        ReflectionTestUtils.setField(readinessService, "profileWeight", 15);
        ReflectionTestUtils.setField(readinessService, "skillsWeight", 10);
        ReflectionTestUtils.setField(readinessService, "companyWeight", 10);

        // Setup properties for RecommendationEngine and HealthService
        ReflectionTestUtils.setField(recommendationEngine, "atsThreshold", 80);
        ReflectionTestUtils.setField(recommendationEngine, "resumeDaysLimit", 30);
        ReflectionTestUtils.setField(recommendationEngine, "interviewDaysLimit", 30);

        ReflectionTestUtils.setField(dashboardHealthService, "resumeDaysLimit", 30);
        ReflectionTestUtils.setField(dashboardHealthService, "interviewDaysLimit", 30);
        ReflectionTestUtils.setField(dashboardHealthService, "profileDaysLimit", 60);

        // Initialize dashboard orchestrators manually to inject all mocks properly
        dashboardAnalyticsService = new DashboardAnalyticsServiceImpl(
                readinessService,
                recommendationEngine,
                dashboardHealthService,
                skillAnalyticsService,
                eligibilityService
        );
        ReflectionTestUtils.setField(dashboardAnalyticsService, "atsThreshold", 80);

        placementIntelligenceService = new PlacementIntelligenceServiceImpl(
                dataAggregator,
                dashboardAnalyticsService
        );

        student = new User();
        student.setId(1L);
        student.setName("Harsha Varthan");
        student.setEmail("harsha@gmail.com");
        student.setCreatedAt(LocalDateTime.now().minusDays(10));
        student.setUpdatedAt(LocalDateTime.now().minusDays(5));
    }

    @Test
    void testState1_NewUser() {
        // Mock DB behavior for empty state
        when(resumeRepository.findByUserId(1L)).thenReturn(Optional.empty());
        when(interviewEvaluationRepository.findLatestEvaluationsByUserId(eq(1L), any(Pageable.class)))
                .thenReturn(Collections.emptyList());
        when(interviewSessionRepository.countByUserAndModeAndStatusCompleted(eq(1L), any(InterviewMode.class)))
                .thenReturn(0L);
        when(interviewSessionQuestionRepository.countAttemptedQuestionsByUserAndMode(eq(1L), any(InterviewMode.class)))
                .thenReturn(0L);
        when(userTargetCompanyRepository.findByUserId(1L)).thenReturn(Collections.emptyList());
        when(targetCompanyRepository.findAll()).thenReturn(Collections.emptyList());
        when(userSkillRepository.findByUserId(1L)).thenReturn(Collections.emptyList());
        when(questionEvaluationRepository.findAllByUserId(1L)).thenReturn(Collections.emptyList());
        when(interviewSessionQuestionRepository.findAllAnsweredQuestionsByUserId(1L)).thenReturn(Collections.emptyList());

        DashboardResponseDto response = placementIntelligenceService.getDashboardResponse(student);

        assertNotNull(response);
        assertEquals("NOT_AVAILABLE", response.placementReadiness().status());
        assertEquals("NOT_AVAILABLE", response.resume().status());
        assertEquals("NOT_AVAILABLE", response.interview().status());
        assertEquals("NOT_AVAILABLE", response.company().status()); // role is empty
        assertEquals("AVAILABLE", response.practice().status());

        // Check readiness score normalized (Resume/Interview weights excluded -> Total weight = 15+10+10 = 35)
        // profile score: name is filled (1 out of 9 fields) -> round(1/9.0 * 100) = 11%
        // skills: 0 user skills, 0 missing -> fallback 0
        // company: 0 target, 0 all -> fallback 0
        // current readiness weighted sum = 11 * 15 = 165. Normalized = round(165 / 35.0) = 5
        assertEquals(5, response.placementReadiness().readinessScore());
        assertEquals("NEEDS_IMPROVEMENT", response.placementReadiness().readinessBand());
        assertEquals("N/A", response.placementReadiness().deltaPercentage());
    }

    @Test
    void testState2_ResumeUploadedButNotAnalyzed() {
        Resume resume = new Resume();
        resume.setId(10L);
        resume.setUploadedAt(LocalDateTime.now().minusDays(1));

        when(resumeRepository.findByUserId(1L)).thenReturn(Optional.of(resume));
        when(resumeAnalysisRepository.findByResume(resume)).thenReturn(Optional.empty());
        when(interviewEvaluationRepository.findLatestEvaluationsByUserId(eq(1L), any(Pageable.class)))
                .thenReturn(Collections.emptyList());
        when(interviewSessionRepository.countByUserAndModeAndStatusCompleted(eq(1L), any(InterviewMode.class)))
                .thenReturn(0L);
        when(interviewSessionQuestionRepository.countAttemptedQuestionsByUserAndMode(eq(1L), any(InterviewMode.class)))
                .thenReturn(0L);
        when(userTargetCompanyRepository.findByUserId(1L)).thenReturn(Collections.emptyList());
        when(targetCompanyRepository.findAll()).thenReturn(Collections.emptyList());
        when(userSkillRepository.findByUserId(1L)).thenReturn(Collections.emptyList());
        when(questionEvaluationRepository.findAllByUserId(1L)).thenReturn(Collections.emptyList());
        when(interviewSessionQuestionRepository.findAllAnsweredQuestionsByUserId(1L)).thenReturn(Collections.emptyList());

        DashboardResponseDto response = placementIntelligenceService.getDashboardResponse(student);

        assertNotNull(response);
        assertEquals("AVAILABLE", response.resume().status());
        assertFalse(response.resume().resumeAnalysisAvailable());
        assertTrue(response.resume().resumeUploaded());
        assertNull(response.resume().latestAts());
    }

    @Test
    void testState3_ResumeAnalyzedButNoInterview() {
        Resume resume = new Resume();
        resume.setId(10L);
        resume.setUploadedAt(LocalDateTime.now().minusDays(1));

        ResumeAnalysis analysis = new ResumeAnalysis();
        analysis.setId(20L);
        analysis.setResume(resume);
        analysis.setAtsScore(85);
        analysis.setUpdatedAt(LocalDateTime.now().minusDays(1));
        analysis.setMissingSkills(List.of("SQL", "Java"));

        when(resumeRepository.findByUserId(1L)).thenReturn(Optional.of(resume));
        when(resumeAnalysisRepository.findByResume(resume)).thenReturn(Optional.of(analysis));
        when(interviewEvaluationRepository.findLatestEvaluationsByUserId(eq(1L), any(Pageable.class)))
                .thenReturn(Collections.emptyList());
        when(interviewSessionRepository.countByUserAndModeAndStatusCompleted(eq(1L), any(InterviewMode.class)))
                .thenReturn(0L);
        when(interviewSessionQuestionRepository.countAttemptedQuestionsByUserAndMode(eq(1L), any(InterviewMode.class)))
                .thenReturn(0L);
        when(userTargetCompanyRepository.findByUserId(1L)).thenReturn(Collections.emptyList());
        when(targetCompanyRepository.findAll()).thenReturn(Collections.emptyList());
        when(userSkillRepository.findByUserId(1L)).thenReturn(Collections.emptyList());
        when(questionEvaluationRepository.findAllByUserId(1L)).thenReturn(Collections.emptyList());
        when(interviewSessionQuestionRepository.findAllAnsweredQuestionsByUserId(1L)).thenReturn(Collections.emptyList());

        DashboardResponseDto response = placementIntelligenceService.getDashboardResponse(student);

        assertNotNull(response);
        assertEquals("AVAILABLE", response.resume().status());
        assertTrue(response.resume().resumeAnalysisAvailable());
        assertEquals(85, response.resume().latestAts());
        assertEquals("NOT_AVAILABLE", response.interview().status());
    }

    @Test
    void testState4_InterviewAvailableButNoEvaluation() {
        // If an interview session exists but is in progress / no evaluation exists, latestEvaluation is empty
        Resume resume = new Resume();
        resume.setId(10L);
        resume.setUploadedAt(LocalDateTime.now().minusDays(1));

        ResumeAnalysis analysis = new ResumeAnalysis();
        analysis.setId(20L);
        analysis.setResume(resume);
        analysis.setAtsScore(85);
        analysis.setUpdatedAt(LocalDateTime.now().minusDays(1));

        when(resumeRepository.findByUserId(1L)).thenReturn(Optional.of(resume));
        when(resumeAnalysisRepository.findByResume(resume)).thenReturn(Optional.of(analysis));
        when(interviewEvaluationRepository.findLatestEvaluationsByUserId(eq(1L), any(Pageable.class)))
                .thenReturn(Collections.emptyList());
        when(interviewSessionRepository.countByUserAndModeAndStatusCompleted(eq(1L), any(InterviewMode.class)))
                .thenReturn(0L);
        when(interviewSessionQuestionRepository.countAttemptedQuestionsByUserAndMode(eq(1L), any(InterviewMode.class)))
                .thenReturn(0L);
        when(userTargetCompanyRepository.findByUserId(1L)).thenReturn(Collections.emptyList());
        when(targetCompanyRepository.findAll()).thenReturn(Collections.emptyList());
        when(userSkillRepository.findByUserId(1L)).thenReturn(Collections.emptyList());
        when(questionEvaluationRepository.findAllByUserId(1L)).thenReturn(Collections.emptyList());
        when(interviewSessionQuestionRepository.findAllAnsweredQuestionsByUserId(1L)).thenReturn(Collections.emptyList());

        DashboardResponseDto response = placementIntelligenceService.getDashboardResponse(student);

        assertNotNull(response);
        assertEquals("NOT_AVAILABLE", response.interview().status());
    }

    @Test
    void testState5_FullyCompletedUserWithTrend() {
        student.setCollege("IIT Madras");
        student.setDegree("B.Tech");
        student.setBranch("CSE");
        student.setCgpa(new BigDecimal("9.0"));
        student.setGraduationYear(2026);
        student.setTargetRole("Backend Engineer");
        student.setProjects("Spring Project");
        student.setInternship("Software Intern");

        Resume resume = new Resume();
        resume.setId(10L);
        resume.setUploadedAt(LocalDateTime.now().minusDays(5));

        ResumeAnalysis analysis = new ResumeAnalysis();
        analysis.setId(20L);
        analysis.setResume(resume);
        analysis.setAtsScore(85);
        analysis.setUpdatedAt(LocalDateTime.now().minusDays(4));

        InterviewSession session1 = new InterviewSession();
        session1.setId(100L);
        session1.setUser(student);

        InterviewEvaluation latestEval = new InterviewEvaluation();
        latestEval.setId(1L);
        latestEval.setInterviewSession(session1);
        latestEval.setOverallScore(82);
        latestEval.setTechnicalScore(85);
        latestEval.setCommunicationScore(80);
        latestEval.setProblemSolvingScore(80);
        latestEval.setConfidenceScore(83);
        latestEval.setVerdict(Verdict.HIRE);
        latestEval.setPerformanceBand(PerformanceBand.EXCELLENT);
        latestEval.setEvaluatedAt(LocalDateTime.now().minusDays(1));

        InterviewSession session2 = new InterviewSession();
        session2.setId(101L);
        session2.setUser(student);

        InterviewEvaluation prevEval = new InterviewEvaluation();
        prevEval.setId(2L);
        prevEval.setInterviewSession(session2);
        prevEval.setOverallScore(72);
        prevEval.setEvaluatedAt(LocalDateTime.now().minusDays(3));

        when(resumeRepository.findByUserId(1L)).thenReturn(Optional.of(resume));
        when(resumeAnalysisRepository.findByResume(resume)).thenReturn(Optional.of(analysis));
        when(interviewEvaluationRepository.findLatestEvaluationsByUserId(eq(1L), any(Pageable.class)))
                .thenReturn(List.of(latestEval, prevEval));
        when(interviewSessionRepository.countByUserAndModeAndStatusCompleted(eq(1L), any(InterviewMode.class)))
                .thenReturn(2L);
        when(interviewSessionQuestionRepository.countAttemptedQuestionsByUserAndMode(eq(1L), any(InterviewMode.class)))
                .thenReturn(10L);

        TargetCompany tc = new TargetCompany();
        tc.setId(500L);
        tc.setName("Google");
        tc.setMinCgpa(new BigDecimal("8.0"));

        UserTargetCompany utc = new UserTargetCompany(student, tc);
        when(userTargetCompanyRepository.findByUserId(1L)).thenReturn(List.of(utc));

        EligibilityResponseDto eligibilityDto = new EligibilityResponseDto(true, true, true, true, Collections.emptySet());
        when(eligibilityService.checkEligibility(student, tc)).thenReturn(eligibilityDto);

        Skill javaSkill = new Skill("Java");
        UserSkill us = new UserSkill(student, javaSkill);
        when(userSkillRepository.findByUserId(1L)).thenReturn(List.of(us));

        when(questionEvaluationRepository.findAllByUserId(1L)).thenReturn(Collections.emptyList());
        when(interviewSessionQuestionRepository.findAllAnsweredQuestionsByUserId(1L)).thenReturn(Collections.emptyList());

        DashboardResponseDto response = placementIntelligenceService.getDashboardResponse(student);

        assertNotNull(response);
        assertEquals("AVAILABLE", response.placementReadiness().status());
        assertEquals("AVAILABLE", response.interview().status());
        assertEquals(82, response.interview().latestInterviewScore());
        assertEquals(10, response.interview().interviewTrend()); // 82 - 72 = 10
        assertEquals("+13.9%", response.interview().deltaPercentage()); // 10 / 72.0 * 100 = 13.88% -> +13.9%

        // Company eligibility card
        assertEquals("AVAILABLE", response.company().status());
        assertEquals(1, response.company().eligibleCompanies());
        assertEquals(1, response.company().targetCompanies());
        assertEquals("You are eligible for 1 out of 1 target companies.", response.company().placementEligibilitySummary());
    }

    @Test
    void testRecommendationDeduplicationAndConfidence() {
        Resume resume = new Resume();
        resume.setId(10L);
        resume.setUploadedAt(LocalDateTime.now().minusDays(1));

        ResumeAnalysis analysis = new ResumeAnalysis();
        analysis.setId(20L);
        analysis.setResume(resume);
        analysis.setAtsScore(85);
        analysis.setUpdatedAt(LocalDateTime.now().minusDays(1));
        // Missing skill "SQL"
        analysis.setMissingSkills(List.of("SQL"));

        InterviewSession session = new InterviewSession();
        session.setId(100L);
        session.setUser(student);

        InterviewEvaluation latestEval = new InterviewEvaluation();
        latestEval.setId(1L);
        latestEval.setInterviewSession(session);
        latestEval.setOverallScore(60);
        latestEval.setVerdict(Verdict.NO_HIRE);
        latestEval.setPerformanceBand(PerformanceBand.AVERAGE);
        latestEval.setEvaluatedAt(LocalDateTime.now().minusDays(1));
        // Weaknesses contain "sql"
        latestEval.setWeaknesses(List.of("SQL query design"));
        latestEval.setRecommendedTopics(List.of("SQL Joins"));

        when(resumeRepository.findByUserId(1L)).thenReturn(Optional.of(resume));
        when(resumeAnalysisRepository.findByResume(resume)).thenReturn(Optional.of(analysis));
        when(interviewEvaluationRepository.findLatestEvaluationsByUserId(eq(1L), any(Pageable.class)))
                .thenReturn(List.of(latestEval));
        when(userSkillRepository.findByUserId(1L)).thenReturn(Collections.emptyList());

        RecommendationCard recCard = recommendationEngine.generateRecommendations(student, dataAggregator.aggregate(student));

        assertNotNull(recCard);
        assertFalse(recCard.recommendations().isEmpty());

        // Find the SQL recommendation
        Optional<RecommendationDto> sqlRec = recCard.recommendations().stream()
                .filter(r -> r.title().contains("SQL"))
                .findFirst();

        assertTrue(sqlRec.isPresent());
        assertEquals("HIGH", sqlRec.get().priority());
        assertEquals(90, sqlRec.get().priorityScore());
        assertEquals(96, sqlRec.get().confidenceScore()); // dynamic calculation
        assertTrue(sqlRec.get().triggeredBy().contains("Resume Analysis"));
        assertTrue(sqlRec.get().triggeredBy().contains("Interview Evaluation"));
    }
}
