package com.placement.platform.job.recommendation;

import com.placement.platform.entity.User;
import com.placement.platform.job.dto.RecommendationAnalyticsResponseDto;
import com.placement.platform.job.entity.Job;
import com.placement.platform.job.repository.JobRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RecommendationAnalyticsServiceTest {

    @Mock
    private JobRecommendationRepository repository;

    @Mock
    private JobRepository jobRepository;

    private RecommendationAnalyticsService service;
    private User user;
    private JobRecommendation rec1;
    private JobRecommendation rec2;
    private Job job1;
    private Job job2;

    @BeforeEach
    void setUp() {
        service = new RecommendationAnalyticsServiceImpl(repository, jobRepository);
        user = new User();
        user.setId(1L);

        job1 = new Job();
        job1.setId(10L);
        job1.setCompany("Google");

        job2 = new Job();
        job2.setId(11L);
        job2.setCompany("Amazon");

        rec1 = new JobRecommendation();
        rec1.setId(100L);
        rec1.setJobId(10L);
        rec1.setMatchScore(90);
        rec1.setConfidenceScore(80);
        rec1.setRecommendationStatus(RecommendationStatus.SAVED);
        rec1.setMissingSkills(List.of("Docker", "Kubernetes"));
        rec1.setGenerationId("GEN1");

        rec2 = new JobRecommendation();
        rec2.setId(101L);
        rec2.setJobId(11L);
        rec2.setMatchScore(80);
        rec2.setConfidenceScore(70);
        rec2.setRecommendationStatus(RecommendationStatus.APPLIED);
        rec2.setApplicationStatus(ApplicationStatus.APPLIED);
        rec2.setMissingSkills(List.of("Docker", "Go"));
        rec2.setGenerationId("GEN1");
    }

    @Test
    void testGetAnalytics_LatestGenerationOnly_Success() {
        when(repository.findLatestGenerationId(1L)).thenReturn(Optional.of("GEN1"));
        when(repository.findByUserIdAndGenerationId(1L, "GEN1")).thenReturn(List.of(rec1, rec2));
        when(jobRepository.findAllById(anyList())).thenReturn(List.of(job1, job2));

        RecommendationAnalyticsResponseDto result = service.getAnalytics(user, false);

        assertEquals(2, result.getTotalRecommendations());
        assertEquals(85.0, result.getAverageMatchScore());
        assertEquals(75.0, result.getAverageConfidence());
        assertEquals(1, result.getSavedCount());
        assertEquals(1, result.getAppliedCount());

        // Rates
        assertEquals(0.5, result.getApplicationRate());
        assertEquals(0.5, result.getSaveRate());

        // Top Companies
        assertTrue(result.getTopCompanies().contains("Google"));
        assertTrue(result.getTopCompanies().contains("Amazon"));

        // Missing Skills (Docker is in both, so it has 2 occurrences)
        assertEquals("Docker", result.getMostMissingSkills().get(0));
    }

    @Test
    void testGetAnalytics_IncludeHistory_Success() {
        JobRecommendation rec3 = new JobRecommendation();
        rec3.setId(102L);
        rec3.setJobId(10L);
        rec3.setMatchScore(95);
        rec3.setConfidenceScore(85);
        rec3.setRecommendationStatus(RecommendationStatus.VIEWED);
        rec3.setMissingSkills(List.of("Python"));
        rec3.setGenerationId("GEN0"); // Older generation

        when(repository.findByUserId(1L)).thenReturn(List.of(rec1, rec2, rec3));
        when(jobRepository.findAllById(anyList())).thenReturn(List.of(job1, job2));

        RecommendationAnalyticsResponseDto result = service.getAnalytics(user, true);

        assertEquals(3, result.getTotalRecommendations());
        assertEquals(2, result.getTopCompanies().size());
        assertEquals(88.33, result.getAverageMatchScore(), 0.01);
    }

    @Test
    void testGetAnalytics_NoRecommendations_ReturnsEmpty() {
        when(repository.findLatestGenerationId(1L)).thenReturn(Optional.empty());

        RecommendationAnalyticsResponseDto result = service.getAnalytics(user, false);

        assertEquals(0L, result.getTotalRecommendations());
        assertEquals(0.0, result.getAverageMatchScore());
        assertTrue(result.getTopCompanies().isEmpty());
        assertTrue(result.getMostMissingSkills().isEmpty());
    }
}
