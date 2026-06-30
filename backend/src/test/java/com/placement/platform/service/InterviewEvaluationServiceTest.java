package com.placement.platform.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.placement.platform.dto.*;
import com.placement.platform.entity.*;
import com.placement.platform.exception.*;
import com.placement.platform.mapper.InterviewEvaluationMapper;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InterviewEvaluationServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private InterviewSessionRepository interviewSessionRepository;
    @Mock
    private InterviewSessionQuestionRepository interviewSessionQuestionRepository;
    @Mock
    private InterviewEvaluationRepository interviewEvaluationRepository;
    @Mock
    private QuestionEvaluationRepository questionEvaluationRepository;
    @Mock
    private ResumeRepository resumeRepository;
    @Mock
    private ResumeAnalysisRepository resumeAnalysisRepository;
    @Mock
    private CandidateContextService candidateContextService;
    @Mock
    private AIService aiService;
    @Mock
    private InterviewEvaluationMapper interviewEvaluationMapper;
    @Mock
    private SecurityContext securityContext;
    @Mock
    private Authentication authentication;

    private ObjectMapper objectMapper = new ObjectMapper();

    private InterviewEvaluationServiceImpl interviewEvaluationService;

    private User student;
    private InterviewSession session;
    private InterviewSessionQuestion question;
    private CandidateContext candidateContext;
    private Resume resume;
    private ResumeAnalysis resumeAnalysis;

    private String validGeminiResponse;

    @BeforeEach
    void setUp() {
        interviewEvaluationService = new InterviewEvaluationServiceImpl(
                userRepository,
                interviewSessionRepository,
                interviewSessionQuestionRepository,
                interviewEvaluationRepository,
                questionEvaluationRepository,
                resumeRepository,
                resumeAnalysisRepository,
                candidateContextService,
                aiService,
                interviewEvaluationMapper,
                objectMapper
        );

        // Security context setup
        student = new User();
        student.setId(1L);
        student.setName("Harsha Varthan");
        student.setEmail("harsha@gmail.com");

        SecurityContextHolder.setContext(securityContext);
        lenient().when(securityContext.getAuthentication()).thenReturn(authentication);
        lenient().when(authentication.isAuthenticated()).thenReturn(true);
        lenient().when(authentication.getName()).thenReturn("harsha@gmail.com");
        lenient().when(userRepository.findByEmail("harsha@gmail.com")).thenReturn(Optional.of(student));

        // Session setup
        session = new InterviewSession();
        session.setId(10L);
        session.setUser(student);
        session.setStatus(InterviewStatus.COMPLETED);
        session.setCompletedAt(LocalDateTime.now());

        // Question setup
        question = new InterviewSessionQuestion();
        question.setId(101L);
        question.setInterviewSession(session);
        question.setQuestionText("Explain Polymorphism");
        question.setCategory(QuestionCategory.BEHAVIORAL);
        question.setDifficulty(InterviewDifficulty.MEDIUM);
        question.setStatus(SessionQuestionStatus.ANSWERED);
        question.setAnswerType(AnswerType.TEXT);
        question.setTextAnswer("Polymorphism is...");

        GlobalQuestion gq = new GlobalQuestion();
        gq.setIdealAnswer("Polymorphism is the ability to take many forms.");
        question.setGlobalQuestion(gq);

        // CandidateContext setup
        candidateContext = new CandidateContext(
                "Harsha Varthan", "College X", "B.Tech", "CSE", BigDecimal.valueOf(9.0),
                "Software Engineer", "Project 1", "Internship 1", List.of("Java"),
                "Resume summary", List.of("Coding"), List.of("Public speaking"), List.of("Docker"),
                List.of("Google")
        );

        // Resume & Analysis setup
        resume = new Resume(student, "resume.pdf", "user_1_resume.pdf", "uploads/resume.pdf", "application/pdf", 1024L);
        resumeAnalysis = new ResumeAnalysis();
        resumeAnalysis.setResume(resume);
        resumeAnalysis.setAtsScore(85);

        // Gemini response JSON setup
        validGeminiResponse = "{" +
                "  \"overallScore\": 85," +
                "  \"technicalScore\": 80," +
                "  \"communicationScore\": 90," +
                "  \"problemSolvingScore\": 85," +
                "  \"confidenceScore\": 85," +
                "  \"profileMatchScore\": 82," +
                "  \"verdict\": \"HIRE\"," +
                "  \"verdictJustification\": \"Excellent candidate justification\"," +
                "  \"summary\": \"Overall summary\"," +
                "  \"overallFeedback\": \"Good overall feedback\"," +
                "  \"strengths\": [\"Java coding\"]," +
                "  \"weaknesses\": [\"None\"]," +
                "  \"recommendedTopics\": [\"concurrency\"]," +
                "  \"learningPlan\": [\"Read books\"]," +
                "  \"questionEvaluations\": [{" +
                "    \"questionId\": 101," +
                "    \"score\": 90," +
                "    \"strengths\": [\"clear definition\"]," +
                "    \"weaknesses\": [\"none\"]," +
                "    \"feedback\": \"great job\"," +
                "    \"improvement\": \"none\"," +
                "    \"idealAnswer\": \"Polymorphism is...\"" +
                "  }]" +
                "}";
    }

    @Test
    void evaluateInterview_Success() {
        when(interviewSessionRepository.findById(10L)).thenReturn(Optional.of(session));
        when(interviewEvaluationRepository.findByInterviewSession(session)).thenReturn(Optional.empty());
        when(interviewSessionQuestionRepository.findByInterviewSessionOrderByDisplayOrderAsc(session))
                .thenReturn(List.of(question));
        when(candidateContextService.buildContext(student)).thenReturn(candidateContext);
        when(resumeRepository.findByUser(student)).thenReturn(Optional.of(resume));
        when(resumeAnalysisRepository.findByResume(resume)).thenReturn(Optional.of(resumeAnalysis));
        when(aiService.evaluateInterview(anyString())).thenReturn(validGeminiResponse);
        when(aiService.getModelUsed()).thenReturn("gemini-2.5-flash");

        InterviewEvaluation savedEvaluation = new InterviewEvaluation();
        savedEvaluation.setInterviewSession(session);
        savedEvaluation.setOverallScore(85);
        savedEvaluation.setPerformanceBand(PerformanceBand.GOOD);
        savedEvaluation.setVerdict(Verdict.HIRE);

        when(interviewEvaluationRepository.save(any(InterviewEvaluation.class))).thenReturn(savedEvaluation);
        when(questionEvaluationRepository.save(any(QuestionEvaluation.class))).thenReturn(new QuestionEvaluation());

        InterviewEvaluationResponseDto expectedDto = new InterviewEvaluationResponseDto(
                1L, 10L, 85, 80, 90, 85, 85, 82, "GOOD", "HIRE", "Justification", "Summary", "Feedback",
                List.of("Java coding"), List.of("None"), List.of("concurrency"), List.of("Read books"),
                "gemini-2.5-flash", "COMPLETED", LocalDateTime.now(), List.of()
        );
        when(interviewEvaluationMapper.toDto(any(), any())).thenReturn(expectedDto);

        InterviewEvaluationResponseDto result = interviewEvaluationService.evaluateInterview(10L);

        assertNotNull(result);
        assertEquals(85, result.overallScore());
        assertEquals("GOOD", result.performanceBand());
        assertEquals("HIRE", result.verdict());

        verify(aiService, times(1)).evaluateInterview(anyString());
        verify(interviewEvaluationRepository, times(1)).save(any(InterviewEvaluation.class));
        verify(questionEvaluationRepository, times(1)).save(any(QuestionEvaluation.class));
    }

    @Test
    void evaluateInterview_CachedResponse_ReturnsCachedResult() {
        when(interviewSessionRepository.findById(10L)).thenReturn(Optional.of(session));

        InterviewEvaluation cachedEvaluation = new InterviewEvaluation();
        cachedEvaluation.setInterviewSession(session);
        cachedEvaluation.setOverallScore(85);

        when(interviewEvaluationRepository.findByInterviewSession(session)).thenReturn(Optional.of(cachedEvaluation));
        when(interviewSessionQuestionRepository.findByInterviewSessionOrderByDisplayOrderAsc(session))
                .thenReturn(List.of(question));
        when(questionEvaluationRepository.findByInterviewSessionQuestionIn(anyList()))
                .thenReturn(List.of(new QuestionEvaluation()));

        InterviewEvaluationResponseDto expectedDto = new InterviewEvaluationResponseDto(
                1L, 10L, 85, 80, 90, 85, 85, 82, "GOOD", "HIRE", "Justification", "Summary", "Feedback",
                List.of("Java coding"), List.of("None"), List.of("concurrency"), List.of("Read books"),
                "gemini-2.5-flash", "COMPLETED", LocalDateTime.now(), List.of()
        );
        when(interviewEvaluationMapper.toDto(any(), any())).thenReturn(expectedDto);

        InterviewEvaluationResponseDto result = interviewEvaluationService.evaluateInterview(10L);

        assertNotNull(result);
        assertEquals(85, result.overallScore());

        // AIService should never be invoked as results are cached
        verify(aiService, never()).evaluateInterview(anyString());
        verify(interviewEvaluationRepository, never()).save(any(InterviewEvaluation.class));
    }

    @Test
    void evaluateInterview_SessionNotCompleted_ThrowsIllegalArgumentException() {
        session.setStatus(InterviewStatus.IN_PROGRESS);
        when(interviewSessionRepository.findById(10L)).thenReturn(Optional.of(session));

        assertThrows(IllegalArgumentException.class, () -> {
            interviewEvaluationService.evaluateInterview(10L);
        });
    }

    @Test
    void evaluateInterview_NoAnsweredQuestions_ThrowsInterviewEvaluationException() {
        question.setStatus(SessionQuestionStatus.NOT_STARTED);
        question.setTextAnswer(null);
        when(interviewSessionRepository.findById(10L)).thenReturn(Optional.of(session));
        when(interviewSessionQuestionRepository.findByInterviewSessionOrderByDisplayOrderAsc(session))
                .thenReturn(List.of(question));

        assertThrows(InterviewEvaluationException.class, () -> {
            interviewEvaluationService.evaluateInterview(10L);
        });
    }

    @Test
    void evaluateInterview_AudioWithoutTranscription_ThrowsInterviewEvaluationException() {
        question.setAnswerType(AnswerType.AUDIO);
        question.setTextAnswer(null);
        question.setAudioFilePath("uploads/audio.wav");

        when(interviewSessionRepository.findById(10L)).thenReturn(Optional.of(session));
        when(interviewSessionQuestionRepository.findByInterviewSessionOrderByDisplayOrderAsc(session))
                .thenReturn(List.of(question));

        InterviewEvaluationException exception = assertThrows(InterviewEvaluationException.class, () -> {
            interviewEvaluationService.evaluateInterview(10L);
        });
        assertTrue(exception.getMessage().contains("Audio evaluation is not yet supported"));
    }

    @Test
    void evaluateInterview_InvalidGeminiJson_ThrowsInterviewEvaluationException() {
        when(interviewSessionRepository.findById(10L)).thenReturn(Optional.of(session));
        when(interviewSessionQuestionRepository.findByInterviewSessionOrderByDisplayOrderAsc(session))
                .thenReturn(List.of(question));
        when(candidateContextService.buildContext(student)).thenReturn(candidateContext);
        when(resumeRepository.findByUser(student)).thenReturn(Optional.of(resume));
        when(resumeAnalysisRepository.findByResume(resume)).thenReturn(Optional.of(resumeAnalysis));
        when(aiService.evaluateInterview(anyString())).thenReturn("{invalid json");

        assertThrows(InterviewEvaluationException.class, () -> {
            interviewEvaluationService.evaluateInterview(10L);
        });
    }

    @Test
    void evaluateInterview_MissingMandatoryFields_ThrowsInterviewEvaluationException() {
        String incompleteResponse = "{" +
                "  \"overallScore\": 85," +
                "  \"technicalScore\": 80," +
                "  \"verdict\": \"HIRE\"" +
                "}";

        when(interviewSessionRepository.findById(10L)).thenReturn(Optional.of(session));
        when(interviewSessionQuestionRepository.findByInterviewSessionOrderByDisplayOrderAsc(session))
                .thenReturn(List.of(question));
        when(candidateContextService.buildContext(student)).thenReturn(candidateContext);
        when(resumeRepository.findByUser(student)).thenReturn(Optional.of(resume));
        when(resumeAnalysisRepository.findByResume(resume)).thenReturn(Optional.of(resumeAnalysis));
        when(aiService.evaluateInterview(anyString())).thenReturn(incompleteResponse);

        assertThrows(InterviewEvaluationException.class, () -> {
            interviewEvaluationService.evaluateInterview(10L);
        });
    }

    @Test
    void evaluateInterview_ScoreOutOfRange_ThrowsInterviewEvaluationException() {
        String invalidScoreResponse = validGeminiResponse.replace("\"overallScore\": 85", "\"overallScore\": 150");

        when(interviewSessionRepository.findById(10L)).thenReturn(Optional.of(session));
        when(interviewSessionQuestionRepository.findByInterviewSessionOrderByDisplayOrderAsc(session))
                .thenReturn(List.of(question));
        when(candidateContextService.buildContext(student)).thenReturn(candidateContext);
        when(resumeRepository.findByUser(student)).thenReturn(Optional.of(resume));
        when(resumeAnalysisRepository.findByResume(resume)).thenReturn(Optional.of(resumeAnalysis));
        when(aiService.evaluateInterview(anyString())).thenReturn(invalidScoreResponse);

        assertThrows(InterviewEvaluationException.class, () -> {
            interviewEvaluationService.evaluateInterview(10L);
        });
    }

    @Test
    void evaluateInterview_InvalidVerdict_ThrowsInterviewEvaluationException() {
        String invalidVerdictResponse = validGeminiResponse.replace("\"verdict\": \"HIRE\"", "\"verdict\": \"SUPERB\"");

        when(interviewSessionRepository.findById(10L)).thenReturn(Optional.of(session));
        when(interviewSessionQuestionRepository.findByInterviewSessionOrderByDisplayOrderAsc(session))
                .thenReturn(List.of(question));
        when(candidateContextService.buildContext(student)).thenReturn(candidateContext);
        when(resumeRepository.findByUser(student)).thenReturn(Optional.of(resume));
        when(resumeAnalysisRepository.findByResume(resume)).thenReturn(Optional.of(resumeAnalysis));
        when(aiService.evaluateInterview(anyString())).thenReturn(invalidVerdictResponse);

        assertThrows(InterviewEvaluationException.class, () -> {
            interviewEvaluationService.evaluateInterview(10L);
        });
    }

    @Test
    void evaluateInterview_QuestionIdMismatch_ThrowsInterviewEvaluationException() {
        String wrongQuestionIdResponse = validGeminiResponse.replace("\"questionId\": 101", "\"questionId\": 999");

        when(interviewSessionRepository.findById(10L)).thenReturn(Optional.of(session));
        when(interviewSessionQuestionRepository.findByInterviewSessionOrderByDisplayOrderAsc(session))
                .thenReturn(List.of(question));
        when(candidateContextService.buildContext(student)).thenReturn(candidateContext);
        when(resumeRepository.findByUser(student)).thenReturn(Optional.of(resume));
        when(resumeAnalysisRepository.findByResume(resume)).thenReturn(Optional.of(resumeAnalysis));
        when(aiService.evaluateInterview(anyString())).thenReturn(wrongQuestionIdResponse);

        assertThrows(InterviewEvaluationException.class, () -> {
            interviewEvaluationService.evaluateInterview(10L);
        });
    }

    @Test
    void evaluateInterview_PerformanceBandMapping() {
        // We will directly verify calculatePerformanceBand behavior via testing scores
        // We can do this since the method is used inside the public evaluateInterviewTransactional method.
        // Let's test that 95 overallScore calculates EXCELLENT, 80 GOOD, 70 AVERAGE, 50 NEEDS_IMPROVEMENT.

        when(interviewSessionRepository.findById(10L)).thenReturn(Optional.of(session));
        when(interviewEvaluationRepository.findByInterviewSession(session)).thenReturn(Optional.empty());
        when(interviewSessionQuestionRepository.findByInterviewSessionOrderByDisplayOrderAsc(session))
                .thenReturn(List.of(question));
        when(candidateContextService.buildContext(student)).thenReturn(candidateContext);
        when(resumeRepository.findByUser(student)).thenReturn(Optional.of(resume));
        when(resumeAnalysisRepository.findByResume(resume)).thenReturn(Optional.of(resumeAnalysis));
        when(aiService.getModelUsed()).thenReturn("gemini-2.5-flash");

        // We run a loop mock for each score
        Map<Integer, PerformanceBand> scoresToBands = new HashMap<>();
        scoresToBands.put(95, PerformanceBand.EXCELLENT);
        scoresToBands.put(80, PerformanceBand.GOOD);
        scoresToBands.put(70, PerformanceBand.AVERAGE);
        scoresToBands.put(50, PerformanceBand.NEEDS_IMPROVEMENT);

        for (Map.Entry<Integer, PerformanceBand> entry : scoresToBands.entrySet()) {
            Integer overallScore = entry.getKey();
            PerformanceBand expectedBand = entry.getValue();

            String customResponse = validGeminiResponse.replace("\"overallScore\": 85", "\"overallScore\": " + overallScore);
            reset(aiService, interviewEvaluationRepository, questionEvaluationRepository);
            when(aiService.evaluateInterview(anyString())).thenReturn(customResponse);
            when(aiService.getModelUsed()).thenReturn("gemini-2.5-flash");

            interviewEvaluationService.evaluateInterviewTransactional(10L);

            // Capture the saved InterviewEvaluation and verify the band
            org.mockito.ArgumentCaptor<InterviewEvaluation> captor = org.mockito.ArgumentCaptor.forClass(InterviewEvaluation.class);
            verify(interviewEvaluationRepository).save(captor.capture());
            assertEquals(expectedBand, captor.getValue().getPerformanceBand());
        }
    }
}
