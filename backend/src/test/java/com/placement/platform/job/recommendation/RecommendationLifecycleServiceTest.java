package com.placement.platform.job.recommendation;

import com.placement.platform.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RecommendationLifecycleServiceTest {

    @Mock
    private JobRecommendationRepository repository;

    private RecommendationLifecycleService service;
    private JobRecommendation recommendation;

    @BeforeEach
    void setUp() {
        service = new RecommendationLifecycleServiceImpl(repository);
        recommendation = new JobRecommendation();
        recommendation.setId(100L);
        recommendation.setRecommendationStatus(RecommendationStatus.NEW);
        recommendation.setViewCount(0);
        recommendation.setHidden(false);
    }

    @Test
    void testMarkViewed_FromNew_Success() {
        when(repository.findById(100L)).thenReturn(Optional.of(recommendation));
        when(repository.save(any(JobRecommendation.class))).thenAnswer(inv -> inv.getArgument(0));

        JobRecommendation result = service.markViewed(100L);

        assertEquals(RecommendationStatus.VIEWED, result.getRecommendationStatus());
        assertEquals(1, result.getViewCount());
        assertNotNull(result.getViewedAt());
        verify(repository).save(recommendation);
    }

    @Test
    void testMarkViewed_FromAlreadyViewed_Success() {
        recommendation.setRecommendationStatus(RecommendationStatus.VIEWED);
        recommendation.setViewCount(1);

        when(repository.findById(100L)).thenReturn(Optional.of(recommendation));
        when(repository.save(any(JobRecommendation.class))).thenAnswer(inv -> inv.getArgument(0));

        JobRecommendation result = service.markViewed(100L);

        assertEquals(RecommendationStatus.VIEWED, result.getRecommendationStatus());
        assertEquals(2, result.getViewCount()); // Increments view count
    }

    @Test
    void testSaveRecommendation_Success() {
        recommendation.setRecommendationStatus(RecommendationStatus.VIEWED);

        when(repository.findById(100L)).thenReturn(Optional.of(recommendation));
        when(repository.save(any(JobRecommendation.class))).thenAnswer(inv -> inv.getArgument(0));

        JobRecommendation result = service.saveRecommendation(100L);

        assertEquals(RecommendationStatus.SAVED, result.getRecommendationStatus());
        assertNotNull(result.getSavedAt());
    }

    @Test
    void testSaveRecommendation_InvalidTransition_ThrowsException() {
        recommendation.setRecommendationStatus(RecommendationStatus.ARCHIVED);

        when(repository.findById(100L)).thenReturn(Optional.of(recommendation));

        assertThrows(IllegalStateException.class, () -> service.saveRecommendation(100L));
        verify(repository, never()).save(any());
    }

    @Test
    void testUnsaveRecommendation_Success() {
        recommendation.setRecommendationStatus(RecommendationStatus.SAVED);

        when(repository.findById(100L)).thenReturn(Optional.of(recommendation));
        when(repository.save(any(JobRecommendation.class))).thenAnswer(inv -> inv.getArgument(0));

        JobRecommendation result = service.unsaveRecommendation(100L);

        assertEquals(RecommendationStatus.VIEWED, result.getRecommendationStatus());
        assertNull(result.getSavedAt());
    }

    @Test
    void testHideRecommendation_Success() {
        recommendation.setRecommendationStatus(RecommendationStatus.VIEWED);

        when(repository.findById(100L)).thenReturn(Optional.of(recommendation));
        when(repository.save(any(JobRecommendation.class))).thenAnswer(inv -> inv.getArgument(0));

        JobRecommendation result = service.hideRecommendation(100L);

        assertEquals(RecommendationStatus.HIDDEN, result.getRecommendationStatus());
        assertTrue(result.getHidden());
        assertNotNull(result.getHiddenAt());
    }

    @Test
    void testArchiveRecommendation_Success() {
        recommendation.setRecommendationStatus(RecommendationStatus.SAVED);

        when(repository.findById(100L)).thenReturn(Optional.of(recommendation));
        when(repository.save(any(JobRecommendation.class))).thenAnswer(inv -> inv.getArgument(0));

        JobRecommendation result = service.archiveRecommendation(100L);

        assertEquals(RecommendationStatus.ARCHIVED, result.getRecommendationStatus());
        assertNotNull(result.getArchivedAt());
    }

    @Test
    void testExpireRecommendation_Success() {
        recommendation.setRecommendationStatus(RecommendationStatus.NEW);

        when(repository.findById(100L)).thenReturn(Optional.of(recommendation));
        when(repository.save(any(JobRecommendation.class))).thenAnswer(inv -> inv.getArgument(0));

        JobRecommendation result = service.expireRecommendation(100L);

        assertEquals(RecommendationStatus.EXPIRED, result.getRecommendationStatus());
    }

    @Test
    void testSubmitFeedback_Success() {
        when(repository.findById(100L)).thenReturn(Optional.of(recommendation));
        when(repository.save(any(JobRecommendation.class))).thenAnswer(inv -> inv.getArgument(0));

        JobRecommendation result = service.submitFeedback(100L, RecommendationFeedbackType.USEFUL, "Great match!");

        assertEquals(RecommendationFeedbackType.USEFUL, result.getFeedbackType());
        assertEquals("Great match!", result.getFeedbackNotes());
    }

    @Test
    void testGetRecommendation_NotFound_ThrowsException() {
        when(repository.findById(100L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.markViewed(100L));
    }
}
