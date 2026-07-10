package com.placement.platform.job.recommendation;

import com.placement.platform.entity.User;
import com.placement.platform.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ApplicationTrackingServiceTest {

    @Mock
    private JobRecommendationRepository repository;

    private ApplicationTrackingService service;
    private JobRecommendation recommendation;

    @BeforeEach
    void setUp() {
        service = new ApplicationTrackingServiceImpl(repository);
        recommendation = new JobRecommendation();
        recommendation.setId(100L);
        recommendation.setRecommendationStatus(RecommendationStatus.VIEWED);
    }

    @Test
    void testApplyForRecommendation_Success() {
        when(repository.findById(100L)).thenReturn(Optional.of(recommendation));
        when(repository.save(any(JobRecommendation.class))).thenAnswer(inv -> inv.getArgument(0));

        JobRecommendation result = service.applyForRecommendation(
                100L, "http://google.com/apply", "REF123", "Applied online");

        assertEquals(RecommendationStatus.APPLIED, result.getRecommendationStatus());
        assertEquals(ApplicationStatus.APPLIED, result.getApplicationStatus());
        assertEquals("http://google.com/apply", result.getApplicationUrl());
        assertEquals("REF123", result.getApplicationReference());
        assertEquals("Applied online", result.getApplicationNotes());
        assertNotNull(result.getAppliedAt());
        assertNotNull(result.getViewedAt());
    }

    @Test
    void testApplyForRecommendation_InvalidTransition_ThrowsException() {
        recommendation.setRecommendationStatus(RecommendationStatus.ARCHIVED);

        when(repository.findById(100L)).thenReturn(Optional.of(recommendation));

        assertThrows(IllegalStateException.class, () -> service.applyForRecommendation(
                100L, "url", "ref", "notes"));
        verify(repository, never()).save(any());
    }

    @Test
    void testUpdateApplicationStage_Success() {
        recommendation.setRecommendationStatus(RecommendationStatus.APPLIED);
        recommendation.setApplicationStatus(ApplicationStatus.APPLIED);

        when(repository.findById(100L)).thenReturn(Optional.of(recommendation));
        when(repository.save(any(JobRecommendation.class))).thenAnswer(inv -> inv.getArgument(0));

        JobRecommendation result = service.updateApplicationStage(100L, ApplicationStatus.TECHNICAL, "Passed interview round");

        assertEquals(ApplicationStatus.TECHNICAL, result.getApplicationStatus());
        assertEquals("Passed interview round", result.getApplicationNotes());
    }

    @Test
    void testUpdateApplicationStage_NoActiveApplication_ThrowsException() {
        // applicationStatus is null
        when(repository.findById(100L)).thenReturn(Optional.of(recommendation));

        assertThrows(IllegalStateException.class, () -> service.updateApplicationStage(100L, ApplicationStatus.TECHNICAL, "Notes"));
    }

    @Test
    void testUpdateApplicationStage_TerminalState_ThrowsException() {
        recommendation.setRecommendationStatus(RecommendationStatus.APPLIED);
        recommendation.setApplicationStatus(ApplicationStatus.JOINED);

        when(repository.findById(100L)).thenReturn(Optional.of(recommendation));

        assertThrows(IllegalStateException.class, () -> service.updateApplicationStage(100L, ApplicationStatus.TECHNICAL, "Notes"));
    }

    @Test
    void testGetAppliedRecommendations_Success() {
        User user = new User();
        user.setId(1L);
        Pageable pageable = PageRequest.of(0, 10);
        Page<JobRecommendation> page = new PageImpl<>(List.of(recommendation));

        when(repository.findByUserIdAndApplicationStatusNotNullAndHiddenFalse(1L, pageable)).thenReturn(page);

        Page<JobRecommendation> result = service.getAppliedRecommendations(user, pageable);

        assertEquals(1, result.getContent().size());
        assertEquals(recommendation, result.getContent().get(0));
    }
}
