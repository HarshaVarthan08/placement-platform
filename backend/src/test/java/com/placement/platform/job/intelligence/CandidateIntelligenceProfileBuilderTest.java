package com.placement.platform.job.intelligence;

import com.placement.platform.dto.dashboard.PlacementReadinessCard;
import com.placement.platform.dto.dashboard.ReadinessBreakdownDto;
import com.placement.platform.dto.EligibilityResponseDto;
import com.placement.platform.entity.*;
import com.placement.platform.repository.*;
import com.placement.platform.service.EligibilityService;
import com.placement.platform.service.dashboard.PlacementDataAggregator;
import com.placement.platform.service.dashboard.PlacementDataAggregator.PlacementData;
import com.placement.platform.service.dashboard.ReadinessService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CandidateIntelligenceProfileBuilderTest {

    @Mock
    private ResumeRepository resumeRepository;
    @Mock
    private ResumeAnalysisRepository resumeAnalysisRepository;
    @Mock
    private UserSkillRepository userSkillRepository;
    @Mock
    private UserTargetCompanyRepository userTargetCompanyRepository;
    @Mock
    private InterviewEvaluationRepository interviewEvaluationRepository;
    @Mock
    private TargetCompanyRepository targetCompanyRepository;
    @Mock
    private EligibilityService eligibilityService;
    @Mock
    private PlacementDataAggregator placementDataAggregator;
    @Mock
    private ReadinessService readinessService;

    private CandidateIntelligenceProfileBuilder builder;
    private User user;

    @BeforeEach
    void setUp() {
        builder = new CandidateIntelligenceProfileBuilder(
                resumeRepository,
                resumeAnalysisRepository,
                userSkillRepository,
                userTargetCompanyRepository,
                interviewEvaluationRepository,
                targetCompanyRepository,
                eligibilityService,
                placementDataAggregator,
                readinessService
        );

        user = new User();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("john@example.com");
        user.setCgpa(new BigDecimal("9.20"));
        user.setCollege("IIT Delhi");
        user.setDegree("B.Tech");
        user.setBranch("Computer Science");
        user.setTargetRole("Software Engineer");
        user.setInternship("Software Dev Intern at Google");
    }

    @Test
    void testBuildProfile_Success() {
        // Mock profile target companies
        TargetCompany company = new TargetCompany("Google");
        UserTargetCompany utc = new UserTargetCompany(user, company);
        when(userTargetCompanyRepository.findByUserId(1L)).thenReturn(List.of(utc));

        // Mock skills
        Skill skill = new Skill();
        skill.setName("Java");
        UserSkill userSkill = new UserSkill(user, skill);
        when(userSkillRepository.findByUserId(1L)).thenReturn(List.of(userSkill));

        // Mock resume and resume analysis
        Resume resume = new Resume(user, "resume.pdf", "resume.pdf", "/path", "pdf", 1024L);
        when(resumeRepository.findByUserId(1L)).thenReturn(Optional.of(resume));

        ResumeAnalysis analysis = new ResumeAnalysis();
        analysis.setAtsScore(85);
        when(resumeAnalysisRepository.findByResume(resume)).thenReturn(Optional.of(analysis));

        // Mock interview evaluation
        InterviewEvaluation eval = new InterviewEvaluation();
        eval.setTechnicalScore(90);
        eval.setCommunicationScore(85);
        eval.setConfidenceScore(80);
        when(interviewEvaluationRepository.findLatestEvaluationsByUserId(1L, PageRequest.of(0, 1)))
                .thenReturn(List.of(eval));

        // Mock dashboard readiness
        PlacementData data = mock(PlacementData.class);
        when(placementDataAggregator.aggregate(user)).thenReturn(data);

        ReadinessBreakdownDto breakdown = new ReadinessBreakdownDto(85, 90, 100, 100, 100, 30, 35, 15, 10, 10, 25.5, 31.5, 15.0, 10.0, 10.0);
        PlacementReadinessCard readinessCard = new PlacementReadinessCard(
                "AVAILABLE", 92, "EXCELLENT", breakdown, "Explanation", 0, "0%", LocalDateTime.now()
        );
        when(readinessService.calculateReadiness(user, data)).thenReturn(readinessCard);

        // Mock eligibility
        when(targetCompanyRepository.findAll()).thenReturn(List.of(company));
        EligibilityResponseDto eligibilityDto = new EligibilityResponseDto(true, true, true, true, new HashSet<>());
        when(eligibilityService.checkEligibility(user, company)).thenReturn(eligibilityDto);

        // Execute builder
        CandidateIntelligenceProfile profile = builder.buildProfile(user);

        // Assert profile properties
        assertNotNull(profile);
        assertEquals(1L, profile.userId());
        assertEquals(List.of("Software Engineer"), profile.preferredRoles());
        assertEquals(List.of("Google"), profile.preferredCompanies());
        assertEquals(List.of("Java"), profile.skills());
        assertEquals(1, profile.experience());
        assertEquals("B.Tech in Computer Science from IIT Delhi", profile.education());
        assertEquals(new BigDecimal("9.20"), profile.cgpa());
        assertEquals(85, profile.resumeATSScore());
        assertEquals(List.of("Java"), profile.resumeExtractedSkills());
        assertEquals(90, profile.interviewTechnicalScore());
        assertEquals(85, profile.interviewCommunicationScore());
        assertEquals(80, profile.interviewConfidenceScore());
        assertEquals(92, profile.placementReadinessScore());
        assertEquals(List.of("Google"), profile.eligibleCompanies());
    }
}
