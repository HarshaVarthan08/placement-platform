package com.placement.platform.service;

import com.placement.platform.dto.*;
import com.placement.platform.entity.*;
import com.placement.platform.exception.*;
import com.placement.platform.mapper.InterviewMapper;
import com.placement.platform.repository.*;
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
import java.util.*;

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
    private GlobalQuestionRepository globalQuestionRepository;

    @Mock
    private InterviewQuestionRepository interviewQuestionRepository;

    @Mock
    private InterviewQuestionExposureRepository interviewQuestionExposureRepository;

    @Mock
    private InterviewSessionQuestionRepository interviewSessionQuestionRepository;

    @Mock
    private InterviewQuestionPoolRepository interviewQuestionPoolRepository;

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
        ReflectionTestUtils.setField(interviewService, "learningQuestionsCount", 10);
        ReflectionTestUtils.setField(interviewService, "mockQuestionsCount", 15);
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
        assertTrue(response.remainingHours() > 0 && response.remainingHours() <= 8);
    }

    @Test
    void startInterview_LearningMode_Success() {
        StartInterviewRequestDto request = new StartInterviewRequestDto(InterviewMode.LEARNING, InterviewDifficulty.MEDIUM);
        
        when(interviewSessionRepository.existsByUserAndStatus(student, InterviewStatus.IN_PROGRESS)).thenReturn(false);
        when(interviewProfileRepository.findByUser(student)).thenReturn(Optional.empty());

        InterviewQuestionPool pool = new InterviewQuestionPool();
        pool.setId(5L);
        pool.setProfileVersion(1);
        when(interviewQuestionPoolRepository.findByUserAndStatus(student, QuestionPoolStatus.ACTIVE))
                .thenReturn(Optional.of(pool));

        // Mock 4 personalized questions
        List<InterviewQuestion> pQuestions = new ArrayList<>();
        for (long i = 1; i <= 4; i++) {
            InterviewQuestion q = new InterviewQuestion();
            q.setId(i);
            q.setQuestion("Personalized Q" + i);
            q.setDifficulty(InterviewDifficulty.MEDIUM);
            pQuestions.add(q);
        }
        when(interviewQuestionRepository.findByQuestionPool(pool)).thenReturn(pQuestions);

        // Mock 6 global questions
        List<GlobalQuestion> gQuestions = new ArrayList<>();
        for (long i = 1; i <= 6; i++) {
            GlobalQuestion q = new GlobalQuestion();
            q.setId(i);
            q.setQuestion("Global Q" + i);
            q.setDifficulty(InterviewDifficulty.MEDIUM);
            gQuestions.add(q);
        }
        when(globalQuestionRepository.findAll()).thenReturn(gQuestions);

        when(interviewQuestionExposureRepository.findByUserAndMode(student, InterviewMode.LEARNING))
                .thenReturn(Collections.emptyList());

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
        verify(interviewSessionQuestionRepository, times(1)).saveAll(anyList());
    }

    @Test
    void startInterview_MockMode_Success() {
        StartInterviewRequestDto request = new StartInterviewRequestDto(InterviewMode.MOCK, InterviewDifficulty.HARD);
        
        when(interviewSessionRepository.existsByUserAndStatus(student, InterviewStatus.IN_PROGRESS)).thenReturn(false);
        when(interviewSessionRepository.findFirstByUserAndModeOrderByStartedAtDesc(student, InterviewMode.MOCK))
                .thenReturn(Optional.empty());

        InterviewProfile profile = new InterviewProfile();
        profile.setProfileVersion(3);
        when(interviewProfileRepository.findByUser(student)).thenReturn(Optional.of(profile));

        InterviewQuestionPool pool = new InterviewQuestionPool();
        pool.setId(5L);
        pool.setProfileVersion(3);
        when(interviewQuestionPoolRepository.findByUserAndStatus(student, QuestionPoolStatus.ACTIVE))
                .thenReturn(Optional.of(pool));

        // Mock 15 personalized questions with varying categories
        List<InterviewQuestion> pQuestions = new ArrayList<>();
        QuestionCategory[] categories = QuestionCategory.values();
        for (long i = 1; i <= 15; i++) {
            InterviewQuestion q = new InterviewQuestion();
            q.setId(i);
            q.setQuestion("Personalized Q" + i);
            q.setCategory(categories[(int) (i % categories.length)]);
            q.setDifficulty(InterviewDifficulty.HARD);
            pQuestions.add(q);
        }
        when(interviewQuestionRepository.findByQuestionPool(pool)).thenReturn(pQuestions);

        when(interviewQuestionExposureRepository.findByUserAndMode(student, InterviewMode.MOCK))
                .thenReturn(Collections.emptyList());

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
            assertEquals(15, session.getTotalQuestions());
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

    @Test
    void getCurrentQuestion_NoActiveSession_ThrowsException() {
        when(interviewSessionRepository.findByUserAndStatus(student, InterviewStatus.IN_PROGRESS))
                .thenReturn(Optional.empty());

        assertThrows(InterviewSessionNotFoundException.class, () -> {
            interviewService.getCurrentQuestion();
        });
    }

    @Test
    void getCurrentQuestion_ActiveSession_ReturnsQuestion() {
        InterviewSession session = new InterviewSession();
        session.setId(50L);
        session.setCurrentQuestionIndex(0);
        when(interviewSessionRepository.findByUserAndStatus(student, InterviewStatus.IN_PROGRESS))
                .thenReturn(Optional.of(session));

        InterviewSessionQuestion question = new InterviewSessionQuestion();
        question.setId(100L);
        question.setQuestionText("What is polymorphism?");
        question.setTopic("OOP");
        question.setDifficulty(InterviewDifficulty.MEDIUM);
        question.setCategory(QuestionCategory.TECHNICAL);
        question.setDisplayOrder(1);
        question.setQuestionSource(QuestionSource.PERSONALIZED);
        question.setStatus(SessionQuestionStatus.CURRENT);

        when(interviewSessionQuestionRepository.findByInterviewSessionOrderByDisplayOrderAsc(session))
                .thenReturn(Collections.singletonList(question));

        InterviewQuestionResponseDto response = interviewService.getCurrentQuestion();

        assertNotNull(response);
        assertEquals(100L, response.sessionQuestionId());
        assertEquals("What is polymorphism?", response.question());
        assertEquals("OOP", response.topic());
        assertEquals(InterviewDifficulty.MEDIUM, response.difficulty());
        assertEquals(QuestionCategory.TECHNICAL, response.category());
        assertEquals(1, response.displayOrder());
        assertEquals(QuestionSource.PERSONALIZED, response.questionSource());
        assertEquals(SessionQuestionStatus.CURRENT, response.status());
    }

    @Test
    void answerCurrentQuestion_ValidTextAnswer_Success() {
        InterviewSession session = new InterviewSession();
        session.setId(50L);
        session.setCurrentQuestionIndex(0);
        when(interviewSessionRepository.findByUserAndStatus(student, InterviewStatus.IN_PROGRESS))
                .thenReturn(Optional.of(session));

        InterviewSessionQuestion question = new InterviewSessionQuestion();
        question.setId(100L);
        question.setStatus(SessionQuestionStatus.CURRENT);
        question.setQuestionStartedAt(LocalDateTime.now().minusSeconds(30));

        when(interviewSessionQuestionRepository.findByInterviewSessionOrderByDisplayOrderAsc(session))
                .thenReturn(Collections.singletonList(question));

        SubmitAnswerRequestDto request = new SubmitAnswerRequestDto(AnswerType.TEXT, "Polymorphism allows objects to take multiple forms.", null, 30);

        interviewService.answerCurrentQuestion(request);

        assertEquals(SessionQuestionStatus.ANSWERED, question.getStatus());
        assertEquals(AnswerType.TEXT, question.getAnswerType());
        assertEquals("Polymorphism allows objects to take multiple forms.", question.getTextAnswer());
        assertNull(question.getAudioFilePath());
        assertNotNull(question.getAnsweredAt());
        assertTrue(question.getTimeTakenSeconds() >= 30);
        verify(interviewSessionQuestionRepository, times(1)).save(question);
    }

    @Test
    void answerCurrentQuestion_DoubleAnswer_ThrowsException() {
        InterviewSession session = new InterviewSession();
        session.setId(50L);
        session.setCurrentQuestionIndex(0);
        when(interviewSessionRepository.findByUserAndStatus(student, InterviewStatus.IN_PROGRESS))
                .thenReturn(Optional.of(session));

        InterviewSessionQuestion question = new InterviewSessionQuestion();
        question.setId(100L);
        question.setStatus(SessionQuestionStatus.ANSWERED);

        when(interviewSessionQuestionRepository.findByInterviewSessionOrderByDisplayOrderAsc(session))
                .thenReturn(Collections.singletonList(question));

        SubmitAnswerRequestDto request = new SubmitAnswerRequestDto(AnswerType.TEXT, "Polymorphism allows...", null, 30);

        assertThrows(IllegalStateException.class, () -> {
            interviewService.answerCurrentQuestion(request);
        });
    }

    @Test
    void answerCurrentQuestion_InvalidAnswer_ThrowsException() {
        SubmitAnswerRequestDto invalidRequest = new SubmitAnswerRequestDto(AnswerType.TEXT, "   ", null, 30);

        assertThrows(IllegalArgumentException.class, () -> {
            interviewService.answerCurrentQuestion(invalidRequest);
        });
    }

    @Test
    void goToNextQuestion_Success() {
        InterviewSession session = new InterviewSession();
        session.setId(50L);
        session.setCurrentQuestionIndex(0);
        session.setTotalQuestions(2);
        when(interviewSessionRepository.findByUserAndStatus(student, InterviewStatus.IN_PROGRESS))
                .thenReturn(Optional.of(session));

        InterviewSessionQuestion q1 = new InterviewSessionQuestion();
        q1.setId(100L);
        q1.setDisplayOrder(1);
        q1.setStatus(SessionQuestionStatus.ANSWERED);

        InterviewSessionQuestion q2 = new InterviewSessionQuestion();
        q2.setId(101L);
        q2.setDisplayOrder(2);
        q2.setStatus(SessionQuestionStatus.NOT_STARTED);

        List<InterviewSessionQuestion> questions = Arrays.asList(q1, q2);
        when(interviewSessionQuestionRepository.findByInterviewSessionOrderByDisplayOrderAsc(session))
                .thenReturn(questions);

        InterviewProgressResponseDto progress = interviewService.goToNextQuestion();

        assertEquals(1, session.getCurrentQuestionIndex());
        assertEquals(SessionQuestionStatus.CURRENT, q2.getStatus());
        assertNotNull(q2.getQuestionStartedAt());
        verify(interviewSessionRepository, times(1)).save(session);
        verify(interviewSessionQuestionRepository, times(1)).save(q2);
    }

    @Test
    void goToNextQuestion_CurrentUnanswered_ThrowsException() {
        InterviewSession session = new InterviewSession();
        session.setId(50L);
        session.setCurrentQuestionIndex(0);
        when(interviewSessionRepository.findByUserAndStatus(student, InterviewStatus.IN_PROGRESS))
                .thenReturn(Optional.of(session));

        InterviewSessionQuestion q1 = new InterviewSessionQuestion();
        q1.setId(100L);
        q1.setStatus(SessionQuestionStatus.CURRENT);

        when(interviewSessionQuestionRepository.findByInterviewSessionOrderByDisplayOrderAsc(session))
                .thenReturn(Collections.singletonList(q1));

        assertThrows(IllegalStateException.class, () -> {
            interviewService.goToNextQuestion();
        });
    }

    @Test
    void completeInterview_Success() {
        InterviewSession session = new InterviewSession();
        session.setId(50L);
        session.setMode(InterviewMode.MOCK);
        session.setStatus(InterviewStatus.IN_PROGRESS);
        session.setTotalQuestions(1);

        when(interviewSessionRepository.findByUserAndStatus(student, InterviewStatus.IN_PROGRESS))
                .thenReturn(Optional.of(session));

        InterviewSessionQuestion q1 = new InterviewSessionQuestion();
        q1.setStatus(SessionQuestionStatus.ANSWERED);
        when(interviewSessionQuestionRepository.findByInterviewSessionOrderByDisplayOrderAsc(session))
                .thenReturn(Collections.singletonList(q1));
        
        when(interviewSessionRepository.save(session)).thenReturn(session);

        interviewService.completeInterview();

        assertEquals(InterviewStatus.COMPLETED, session.getStatus());
        assertNotNull(session.getCompletedAt());
        assertNotNull(session.getCooldownUntil());
        verify(interviewSessionRepository, times(1)).save(session);
    }

    @Test
    void completeInterview_UnansweredQuestions_ThrowsException() {
        InterviewSession session = new InterviewSession();
        session.setId(50L);
        session.setMode(InterviewMode.MOCK);
        session.setStatus(InterviewStatus.IN_PROGRESS);
        session.setTotalQuestions(1);

        when(interviewSessionRepository.findByUserAndStatus(student, InterviewStatus.IN_PROGRESS))
                .thenReturn(Optional.of(session));

        InterviewSessionQuestion q1 = new InterviewSessionQuestion();
        q1.setStatus(SessionQuestionStatus.CURRENT);
        when(interviewSessionQuestionRepository.findByInterviewSessionOrderByDisplayOrderAsc(session))
                .thenReturn(Collections.singletonList(q1));

        assertThrows(IllegalStateException.class, () -> {
            interviewService.completeInterview();
        });

        verify(interviewSessionRepository, never()).save(session);
    }
}
