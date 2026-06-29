package com.placement.platform.service;

import com.placement.platform.dto.InterviewAvailabilityResponseDto;
import com.placement.platform.dto.InterviewSessionResponseDto;
import com.placement.platform.dto.StartInterviewRequestDto;
import com.placement.platform.entity.*;
import com.placement.platform.exception.InterviewCooldownException;
import com.placement.platform.exception.UserNotFoundException;
import com.placement.platform.mapper.InterviewMapper;
import com.placement.platform.repository.InterviewProfileRepository;
import com.placement.platform.repository.InterviewSessionRepository;
import com.placement.platform.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InterviewServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private InterviewSessionRepository interviewSessionRepository;

    @Mock
    private InterviewProfileRepository interviewProfileRepository;

    @Mock
    private InterviewMapper interviewMapper;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private InterviewServiceImpl interviewService;

    private User student;

    @BeforeEach
    void setUp() {
        student = new User();
        student.setId(1L);
        student.setName("Harsha Varthan");
        student.setEmail("harsha@gmail.com");

        // Mock Security Context
        SecurityContextHolder.setContext(securityContext);
        lenient().when(securityContext.getAuthentication()).thenReturn(authentication);
        lenient().when(authentication.isAuthenticated()).thenReturn(true);
        lenient().when(authentication.getName()).thenReturn("harsha@gmail.com");
        lenient().when(userRepository.findByEmail("harsha@gmail.com")).thenReturn(Optional.of(student));

        // Inject configuration properties default values via ReflectionTestUtils
        ReflectionTestUtils.setField(interviewService, "cooldownHours", 12);
        ReflectionTestUtils.setField(interviewService, "defaultTotalQuestions", 5);
    }

    @Test
    void checkAvailability_NoSessions_ReturnsAvailable() {
        when(interviewSessionRepository.findFirstByUserAndModeOrderByStartedAtDesc(student, InterviewMode.MOCK))
                .thenReturn(Optional.empty());

        InterviewAvailabilityResponseDto response = interviewService.checkAvailability();

        assertTrue(response.available());
        assertNull(response.nextAvailableAt());
        assertEquals(0, response.remainingHours());
        assertEquals(0, response.remainingMinutes());
    }

    @Test
    void checkAvailability_SessionInProgress_ReturnsNotAvailable() {
        InterviewSession mockSession = new InterviewSession();
        mockSession.setId(10L);
        mockSession.setUser(student);
        mockSession.setMode(InterviewMode.MOCK);
        mockSession.setStatus(InterviewStatus.IN_PROGRESS);

        when(interviewSessionRepository.findFirstByUserAndModeOrderByStartedAtDesc(student, InterviewMode.MOCK))
                .thenReturn(Optional.of(mockSession));

        InterviewAvailabilityResponseDto response = interviewService.checkAvailability();

        assertFalse(response.available());
        assertNull(response.nextAvailableAt());
        assertEquals(0, response.remainingHours());
        assertEquals(0, response.remainingMinutes());
    }

    @Test
    void checkAvailability_SessionCompletedWithinCooldown_ReturnsNotAvailable() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime completedAt = now.minusHours(4); // Finished 4 hours ago (8 hours cooldown remaining)
        LocalDateTime cooldownUntil = completedAt.plusHours(12);

        InterviewSession mockSession = new InterviewSession();
        mockSession.setId(10L);
        mockSession.setUser(student);
        mockSession.setMode(InterviewMode.MOCK);
        mockSession.setStatus(InterviewStatus.COMPLETED);
        mockSession.setCompletedAt(completedAt);
        mockSession.setCooldownUntil(cooldownUntil);

        when(interviewSessionRepository.findFirstByUserAndModeOrderByStartedAtDesc(student, InterviewMode.MOCK))
                .thenReturn(Optional.of(mockSession));

        InterviewAvailabilityResponseDto response = interviewService.checkAvailability();

        assertFalse(response.available());
        assertEquals(cooldownUntil, response.nextAvailableAt());
        // Remaining cooldown should be around 7 or 8 hours
        assertTrue(response.remainingHours() > 0 && response.remainingHours() <= 8);
    }

    @Test
    void checkAvailability_SessionCompletedAfterCooldown_ReturnsAvailable() {
        LocalDateTime completedAt = LocalDateTime.now().minusHours(13); // Finished 13 hours ago (cooldown expired)
        LocalDateTime cooldownUntil = completedAt.plusHours(12);

        InterviewSession mockSession = new InterviewSession();
        mockSession.setId(10L);
        mockSession.setUser(student);
        mockSession.setMode(InterviewMode.MOCK);
        mockSession.setStatus(InterviewStatus.COMPLETED);
        mockSession.setCompletedAt(completedAt);
        mockSession.setCooldownUntil(cooldownUntil);

        when(interviewSessionRepository.findFirstByUserAndModeOrderByStartedAtDesc(student, InterviewMode.MOCK))
                .thenReturn(Optional.of(mockSession));

        InterviewAvailabilityResponseDto response = interviewService.checkAvailability();

        assertTrue(response.available());
        assertNull(response.nextAvailableAt());
        assertEquals(0, response.remainingHours());
        assertEquals(0, response.remainingMinutes());
    }

    @Test
    void startInterview_LearningMode_Success() {
        StartInterviewRequestDto request = new StartInterviewRequestDto(InterviewMode.LEARNING, InterviewDifficulty.MEDIUM);
        
        when(interviewSessionRepository.existsByUserAndStatus(student, InterviewStatus.IN_PROGRESS)).thenReturn(false);
        when(interviewProfileRepository.findByUser(student)).thenReturn(Optional.empty());

        InterviewSession savedSession = new InterviewSession();
        savedSession.setId(20L);
        savedSession.setMode(InterviewMode.LEARNING);
        savedSession.setStatus(InterviewStatus.IN_PROGRESS);
        savedSession.setStartedAt(LocalDateTime.now());

        when(interviewSessionRepository.save(any(InterviewSession.class))).thenReturn(savedSession);
        
        InterviewSessionResponseDto mockDto = new InterviewSessionResponseDto(20L, InterviewMode.LEARNING, InterviewStatus.IN_PROGRESS, savedSession.getStartedAt());
        when(interviewMapper.toDto(savedSession)).thenReturn(mockDto);

        InterviewSessionResponseDto result = interviewService.startInterview(request);

        assertNotNull(result);
        assertEquals(20L, result.sessionId());
        assertEquals(InterviewMode.LEARNING, result.mode());
        assertEquals(InterviewStatus.IN_PROGRESS, result.status());

        verify(interviewSessionRepository, times(1)).save(any(InterviewSession.class));
    }

    @Test
    void startInterview_MockMode_Success() {
        StartInterviewRequestDto request = new StartInterviewRequestDto(InterviewMode.MOCK, InterviewDifficulty.HARD);
        
        when(interviewSessionRepository.existsByUserAndStatus(student, InterviewStatus.IN_PROGRESS)).thenReturn(false);
        // Mock checkAvailability to return true
        when(interviewSessionRepository.findFirstByUserAndModeOrderByStartedAtDesc(student, InterviewMode.MOCK))
                .thenReturn(Optional.empty());

        // Profile mapping
        InterviewProfile profile = new InterviewProfile();
        profile.setProfileVersion(3);
        when(interviewProfileRepository.findByUser(student)).thenReturn(Optional.of(profile));

        InterviewSession savedSession = new InterviewSession();
        savedSession.setId(30L);
        savedSession.setMode(InterviewMode.MOCK);
        savedSession.setStatus(InterviewStatus.IN_PROGRESS);
        savedSession.setStartedAt(LocalDateTime.now());

        when(interviewSessionRepository.save(any(InterviewSession.class))).thenReturn(savedSession);
        
        InterviewSessionResponseDto mockDto = new InterviewSessionResponseDto(30L, InterviewMode.MOCK, InterviewStatus.IN_PROGRESS, savedSession.getStartedAt());
        when(interviewMapper.toDto(savedSession)).thenReturn(mockDto);

        InterviewSessionResponseDto result = interviewService.startInterview(request);

        assertNotNull(result);
        assertEquals(30L, result.sessionId());
        assertEquals(InterviewMode.MOCK, result.mode());

        verify(interviewSessionRepository, times(1)).save(argThat(session -> {
            assertEquals(3, session.getProfileVersionUsed());
            assertEquals(5, session.getTotalQuestions());
            return true;
        }));
    }

    @Test
    void startInterview_MockMode_CooldownActive_ThrowsInterviewCooldownException() {
        StartInterviewRequestDto request = new StartInterviewRequestDto(InterviewMode.MOCK, InterviewDifficulty.EASY);

        when(interviewSessionRepository.existsByUserAndStatus(student, InterviewStatus.IN_PROGRESS)).thenReturn(false);

        LocalDateTime completedAt = LocalDateTime.now().minusHours(2);
        LocalDateTime cooldownUntil = completedAt.plusHours(12);

        InterviewSession mockSession = new InterviewSession();
        mockSession.setId(10L);
        mockSession.setUser(student);
        mockSession.setMode(InterviewMode.MOCK);
        mockSession.setStatus(InterviewStatus.COMPLETED);
        mockSession.setCompletedAt(completedAt);
        mockSession.setCooldownUntil(cooldownUntil);

        when(interviewSessionRepository.findFirstByUserAndModeOrderByStartedAtDesc(student, InterviewMode.MOCK))
                .thenReturn(Optional.of(mockSession));

        assertThrows(InterviewCooldownException.class, () -> {
            interviewService.startInterview(request);
        });

        verify(interviewSessionRepository, never()).save(any(InterviewSession.class));
    }

    @Test
    void startInterview_AlreadyInProgressSession_ThrowsInterviewCooldownException() {
        StartInterviewRequestDto request = new StartInterviewRequestDto(InterviewMode.LEARNING, InterviewDifficulty.MEDIUM);
        
        when(interviewSessionRepository.existsByUserAndStatus(student, InterviewStatus.IN_PROGRESS)).thenReturn(true);

        assertThrows(InterviewCooldownException.class, () -> {
            interviewService.startInterview(request);
        });

        verify(interviewSessionRepository, never()).save(any(InterviewSession.class));
    }
}
