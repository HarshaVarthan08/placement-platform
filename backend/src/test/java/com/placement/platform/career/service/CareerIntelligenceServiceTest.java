package com.placement.platform.career.service;

import com.placement.platform.career.intelligence.CareerIntelligenceEngine;
import com.placement.platform.career.intelligence.CareerIntelligenceProfile;
import com.placement.platform.entity.User;
import com.placement.platform.entity.Role;
import com.placement.platform.exception.ResourceNotFoundException;
import com.placement.platform.job.entity.Job;
import com.placement.platform.job.recommendation.JobRecommendation;
import com.placement.platform.job.recommendation.JobRecommendationRepository;
import com.placement.platform.job.repository.JobRepository;
import com.placement.platform.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CareerIntelligenceServiceTest {

    @Mock private CareerIntelligenceEngine engine;
    @Mock private JobRecommendationRepository recommendationRepository;
    @Mock private UserRepository userRepository;
    @Mock private JobRepository jobRepository;

    private CareerIntelligenceService service;

    private User studentUser;
    private User otherStudentUser;
    private User adminUser;
    private JobRecommendation recommendation;
    private Job job;

    @BeforeEach
    void setUp() {
        service = new CareerIntelligenceServiceImpl(
                engine,
                recommendationRepository,
                userRepository,
                jobRepository
        );

        studentUser = new User();
        studentUser.setId(1L);
        studentUser.setRole(Role.STUDENT);

        otherStudentUser = new User();
        otherStudentUser.setId(2L);
        otherStudentUser.setRole(Role.STUDENT);

        adminUser = new User();
        adminUser.setId(99L);
        adminUser.setRole(Role.ADMIN);

        recommendation = new JobRecommendation();
        recommendation.setId(22L);
        recommendation.setUserId(1L);
        recommendation.setJobId(10L);

        job = new Job();
        job.setId(10L);
    }

    @Test
    void testGetProfile_Success_Self() {
        when(recommendationRepository.findById(22L)).thenReturn(Optional.of(recommendation));
        when(userRepository.findById(1L)).thenReturn(Optional.of(studentUser));
        when(jobRepository.findById(10L)).thenReturn(Optional.of(job));

        CareerIntelligenceProfile mockProfile = mock(CareerIntelligenceProfile.class);
        when(engine.buildProfile(studentUser, recommendation, job)).thenReturn(mockProfile);

        CareerIntelligenceProfile result = service.getProfile(22L, studentUser);

        assertNotNull(result);
        assertEquals(mockProfile, result);
        verify(engine, times(1)).buildProfile(studentUser, recommendation, job);
    }

    @Test
    void testGetProfile_Success_Admin() {
        when(recommendationRepository.findById(22L)).thenReturn(Optional.of(recommendation));
        when(userRepository.findById(1L)).thenReturn(Optional.of(studentUser));
        when(jobRepository.findById(10L)).thenReturn(Optional.of(job));

        CareerIntelligenceProfile mockProfile = mock(CareerIntelligenceProfile.class);
        when(engine.buildProfile(studentUser, recommendation, job)).thenReturn(mockProfile);

        CareerIntelligenceProfile result = service.getProfile(22L, adminUser);

        assertNotNull(result);
        assertEquals(mockProfile, result);
    }

    @Test
    void testGetProfile_Forbidden_AccessDenied() {
        when(recommendationRepository.findById(22L)).thenReturn(Optional.of(recommendation));

        assertThrows(AccessDeniedException.class, () -> {
            service.getProfile(22L, otherStudentUser);
        });

        verify(engine, never()).buildProfile(any(), any(), any());
    }

    @Test
    void testGetProfile_NotFound_Recommendation() {
        when(recommendationRepository.findById(22L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            service.getProfile(22L, studentUser);
        });

        verify(engine, never()).buildProfile(any(), any(), any());
    }
}
