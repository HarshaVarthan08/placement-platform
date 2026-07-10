package com.placement.platform.job.recommendation;

import com.placement.platform.entity.User;
import com.placement.platform.job.dto.RecommendationSummaryDto;
import com.placement.platform.job.entity.Job;
import com.placement.platform.job.intelligence.CandidateIntelligenceProfile;
import com.placement.platform.job.intelligence.CandidateIntelligenceProfileBuilder;
import com.placement.platform.job.matching.*;
import com.placement.platform.job.repository.JobRepository;
import com.placement.platform.repository.ResumeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RecommendationServiceTest {

    @Mock
    private CandidateIntelligenceProfileBuilder profileBuilder;
    @Mock
    private JobRepository jobRepository;
    @Mock
    private JobMatchingEngine matchingEngine;
    @Mock
    private JobRecommendationRepository jobRecommendationRepository;
    @Mock
    private ResumeRepository resumeRepository;

    private RecommendationExplanationBuilder explanationBuilder;
    private RecommendationBuilder recommendationBuilder;
    private RecommendationService recommendationService;

    private User user;
    private Job job1;
    private Job job2;
    private CandidateIntelligenceProfile profile;

    @BeforeEach
    void setUp() {
        explanationBuilder = new RecommendationExplanationBuilder();
        recommendationBuilder = new RecommendationBuilder(explanationBuilder);
        recommendationService = new RecommendationServiceImpl(
                profileBuilder,
                jobRepository,
                matchingEngine,
                recommendationBuilder,
                jobRecommendationRepository,
                resumeRepository
        );

        user = new User();
        user.setId(1L);

        job1 = new Job();
        job1.setId(10L);
        job1.setCompany("Google");
        job1.setTitle("SWE");

        job2 = new Job();
        job2.setId(11L);
        job2.setCompany("Amazon");
        job2.setTitle("SDE");

        profile = new CandidateIntelligenceProfile(
                1L, List.of("SWE"), List.of("Google"), List.of("Java"), 0, "Edu", BigDecimal.TEN,
                80, List.of("Java"), 90, 90, 90, 85, List.of("Google"), List.of()
        );
    }

    @Test
    void testRecommendationBuilder_Resolution() {
        // Test level/priority/action mappings
        assertEquals(RecommendationLevel.EXCELLENT, RecommendationBuilder.resolveLevel(92, true));
        assertEquals(RecommendationPriority.HIGH, RecommendationBuilder.resolvePriority(92, true));
        assertEquals(RecommendationAction.APPLY_NOW, RecommendationBuilder.resolveAction(92, true));

        assertEquals(RecommendationLevel.STRONG, RecommendationBuilder.resolveLevel(80, true));
        assertEquals(RecommendationAction.APPLY_WITH_IMPROVED_RESUME, RecommendationBuilder.resolveAction(80, true));

        assertEquals(RecommendationLevel.MODERATE, RecommendationBuilder.resolveLevel(68, true));
        assertEquals(RecommendationAction.IMPROVE_SKILLS, RecommendationBuilder.resolveAction(68, true));

        // Ineligible
        assertEquals(RecommendationLevel.NOT_RECOMMENDED, RecommendationBuilder.resolveLevel(85, false));
        assertEquals(RecommendationPriority.NONE, RecommendationBuilder.resolvePriority(85, false));
        assertEquals(RecommendationAction.NOT_ELIGIBLE, RecommendationBuilder.resolveAction(85, false));
    }

    @Test
    @SuppressWarnings("unchecked")
    void testGenerateRecommendations_Lifecycle() {
        // Set up mock profile and jobs
        when(profileBuilder.buildProfile(user)).thenReturn(profile);
        when(jobRepository.findActiveJobs()).thenReturn(List.of(job1, job2));

        ScoreBreakdown breakdown1 = new ScoreBreakdown(95, 100, 100, 80, 100, 100, 100, 95);
        MatchComponentResult mcr1 = new MatchComponentResult("Eligibility Match", 100, 100, "Eligible", List.of(), List.of());
        RecommendationResult res1 = new RecommendationResult(job1, 95, 100, breakdown1, List.of("Java"), List.of(), List.of(mcr1), 1, 1, 100.0);

        ScoreBreakdown breakdown2 = new ScoreBreakdown(70, 80, 100, 80, 100, 100, 100, 78);
        MatchComponentResult mcr2 = new MatchComponentResult("Eligibility Match", 100, 100, "Eligible", List.of(), List.of());
        RecommendationResult res2 = new RecommendationResult(job2, 78, 100, breakdown2, List.of(), List.of("Java"), List.of(mcr2), 0, 1, 0.0);

        when(matchingEngine.evaluateJobs(profile, List.of(job1, job2))).thenReturn(List.of(res1, res2));

        // Setup existing recommendations in database: job1 has an existing recommendation, job2 does not
        JobRecommendation existing = new JobRecommendation();
        existing.setId(200L);
        existing.setUserId(1L);
        existing.setJobId(10L);
        existing.setMatchScore(90); // will be updated to 95

        when(jobRecommendationRepository.findByUserIdAndJobIdOrderByRecommendationVersionDesc(1L, 10L))
                .thenReturn(List.of(existing));
        when(jobRecommendationRepository.findByUserIdAndJobIdOrderByRecommendationVersionDesc(1L, 11L))
                .thenReturn(List.of());
        when(resumeRepository.findByUserId(1L)).thenReturn(Optional.empty());

        // Mock saving
        when(jobRecommendationRepository.saveAll(any(List.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Execute service
        List<JobRecommendation> savedRecs = recommendationService.generateRecommendations(user);

        // Verify update and creation
        assertEquals(2, savedRecs.size());

        // Check update of existing recommendation for job1
        JobRecommendation rec1 = savedRecs.stream().filter(r -> r.getJobId() == 10L).findFirst().orElse(null);
        assertNotNull(rec1);
        assertNull(rec1.getId()); // new row created for history
        assertEquals(2, rec1.getRecommendationVersion()); // version incremented
        assertEquals(95, rec1.getMatchScore());
        assertEquals(RecommendationLevel.EXCELLENT, rec1.getRecommendationLevel());

        // Check creation of new recommendation for job2
        JobRecommendation rec2 = savedRecs.stream().filter(r -> r.getJobId() == 11L).findFirst().orElse(null);
        assertNotNull(rec2);
        assertNull(rec2.getId()); // new
        assertEquals(78, rec2.getMatchScore());
        assertEquals(RecommendationLevel.STRONG, rec2.getRecommendationLevel());
    }

    @Test
    void testGetRecommendationSummary_Success() {
        JobRecommendation rec1 = new JobRecommendation();
        rec1.setMatchScore(95);
        rec1.setConfidenceScore(100);
        rec1.setRecommendationLevel(RecommendationLevel.EXCELLENT);
        rec1.setJobId(10L);

        JobRecommendation rec2 = new JobRecommendation();
        rec2.setMatchScore(78);
        rec2.setConfidenceScore(90);
        rec2.setRecommendationLevel(RecommendationLevel.STRONG);
        rec2.setJobId(11L);

        rec1.setGenerationId("GEN1");
        rec1.setGeneratedAt(LocalDateTime.now());
        rec2.setGenerationId("GEN1");
        rec2.setGeneratedAt(LocalDateTime.now());

        when(jobRecommendationRepository.findLatestGenerationId(1L)).thenReturn(Optional.of("GEN1"));
        when(jobRecommendationRepository.findByUserIdAndGenerationId(1L, "GEN1")).thenReturn(List.of(rec1, rec2));
        when(jobRepository.findById(10L)).thenReturn(Optional.of(job1)); // job1 is top recommended (highest score 95)

        RecommendationSummaryDto summary = recommendationService.getRecommendationSummary(user);

        assertEquals(2, summary.getTotalRecommendations());
        assertEquals(1, summary.getExcellentMatches());
        assertEquals(1, summary.getStrongMatches());
        assertEquals(86.5, summary.getAverageMatchScore());
        assertEquals(95.0, summary.getAverageConfidence());
        assertEquals("Google", summary.getTopRecommendedCompany());
    }
}
