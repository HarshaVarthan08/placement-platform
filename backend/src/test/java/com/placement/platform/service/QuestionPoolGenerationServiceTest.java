package com.placement.platform.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.placement.platform.dto.CandidateContext;
import com.placement.platform.entity.*;
import com.placement.platform.exception.QuestionPoolGenerationException;
import com.placement.platform.repository.InterviewProfileRepository;
import com.placement.platform.repository.InterviewQuestionPoolRepository;
import com.placement.platform.repository.InterviewQuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class QuestionPoolGenerationServiceTest {

    @Mock
    private CandidateContextService candidateContextService;

    @Mock
    private InterviewProfileService interviewProfileService;

    @Mock
    private InterviewQuestionPoolRepository interviewQuestionPoolRepository;

    @Mock
    private InterviewQuestionRepository interviewQuestionRepository;

    @Mock
    private RestTemplate restTemplate;

    private ObjectMapper objectMapper = new ObjectMapper();

    private QuestionPoolGenerationServiceImpl questionPoolGenerationService;

    private User student;
    private CandidateContext context;
    private InterviewProfile profile;

    @BeforeEach
    void setUp() {
        questionPoolGenerationService = new QuestionPoolGenerationServiceImpl(
                candidateContextService,
                interviewProfileService,
                interviewQuestionPoolRepository,
                interviewQuestionRepository,
                objectMapper
        );

        ReflectionTestUtils.setField(questionPoolGenerationService, "apiKey", "test-key");
        ReflectionTestUtils.setField(questionPoolGenerationService, "model", "gemini-2.5-flash");
        ReflectionTestUtils.setField(questionPoolGenerationService, "restTemplate", restTemplate);

        student = new User();
        student.setId(1L);

        context = new CandidateContext(
                "Harsha Varthan", "IIT Madras", "B.Tech", "CS", new BigDecimal("9.50"),
                "Software Engineer", "Proj1", "Intern1", List.of("Java"),
                "Summary", List.of("Strength"), List.of("Weakness"), List.of("Docker"),
                List.of("Google")
        );

        profile = new InterviewProfile();
        profile.setUser(student);
        profile.setProfileVersion(2);
    }

    @Test
    void generateQuestionPool_Success() throws Exception {
        when(candidateContextService.buildContext(student)).thenReturn(context);
        when(interviewProfileService.getOrCreateProfile(student)).thenReturn(profile);
        when(interviewQuestionPoolRepository.findByUser(student)).thenReturn(Collections.emptyList());

        // Setup mock InterviewQuestionPool save
        when(interviewQuestionPoolRepository.save(any(InterviewQuestionPool.class))).thenAnswer(inv -> {
            InterviewQuestionPool p = inv.getArgument(0);
            p.setId(100L);
            return p;
        });

        // Create a mock valid response JSON representing exactly 50 questions
        String mockResponseText = createMockGeminiResponseJson(15, 15, 10, 5, 5, false);
        String mockResponsePayload = buildGeminiCandidatesPayload(mockResponseText);

        when(restTemplate.postForEntity(anyString(), any(), eq(String.class)))
                .thenReturn(new ResponseEntity<>(mockResponsePayload, HttpStatus.OK));

        InterviewQuestionPool result = questionPoolGenerationService.generateQuestionPool(student, InterviewDifficulty.MEDIUM);

        assertNotNull(result);
        assertEquals(100L, result.getId());
        assertEquals(2, result.getProfileVersion());
        assertEquals("gemini-2.5-flash", result.getModelUsed());
        assertEquals(QuestionPoolStatus.ACTIVE, result.getStatus());

        verify(interviewQuestionRepository, times(50)).save(any(InterviewQuestion.class));
    }

    @Test
    void generateQuestionPool_RetryOnceAndSucceed() throws Exception {
        when(candidateContextService.buildContext(student)).thenReturn(context);
        when(interviewProfileService.getOrCreateProfile(student)).thenReturn(profile);
        when(interviewQuestionPoolRepository.findByUser(student)).thenReturn(Collections.emptyList());
        when(interviewQuestionPoolRepository.save(any(InterviewQuestionPool.class))).thenAnswer(inv -> inv.getArgument(0));

        String mockValidResponseText = createMockGeminiResponseJson(15, 15, 10, 5, 5, false);
        String mockValidPayload = buildGeminiCandidatesPayload(mockValidResponseText);

        // First attempt fails, second succeeds
        when(restTemplate.postForEntity(anyString(), any(), eq(String.class)))
                .thenThrow(new RuntimeException("First attempt network failure"))
                .thenReturn(new ResponseEntity<>(mockValidPayload, HttpStatus.OK));

        InterviewQuestionPool result = questionPoolGenerationService.generateQuestionPool(student, InterviewDifficulty.MEDIUM);

        assertNotNull(result);
        verify(restTemplate, times(2)).postForEntity(anyString(), any(), eq(String.class));
    }

    @Test
    void generateQuestionPool_FailureAfterRetry() throws Exception {
        when(candidateContextService.buildContext(student)).thenReturn(context);

        // Both attempts fail
        when(restTemplate.postForEntity(anyString(), any(), eq(String.class)))
                .thenThrow(new RuntimeException("First network failure"))
                .thenThrow(new RuntimeException("Second network failure"));

        assertThrows(QuestionPoolGenerationException.class, () -> {
            questionPoolGenerationService.generateQuestionPool(student, InterviewDifficulty.MEDIUM);
        });

        verify(restTemplate, times(2)).postForEntity(anyString(), any(), eq(String.class));
    }

    @Test
    void generateQuestionPool_InvalidDistribution_ThrowsException() throws Exception {
        when(candidateContextService.buildContext(student)).thenReturn(context);

        // Incorrect distribution (16 projects instead of 15, 14 technical instead of 15)
        String mockInvalidResponse = createMockGeminiResponseJson(16, 14, 10, 5, 5, false);
        String mockPayload = buildGeminiCandidatesPayload(mockInvalidResponse);

        when(restTemplate.postForEntity(anyString(), any(), eq(String.class)))
                .thenReturn(new ResponseEntity<>(mockPayload, HttpStatus.OK));

        assertThrows(QuestionPoolGenerationException.class, () -> {
            questionPoolGenerationService.generateQuestionPool(student, InterviewDifficulty.MEDIUM);
        });
    }

    @Test
    void generateQuestionPool_DuplicateQuestions_ThrowsException() throws Exception {
        when(candidateContextService.buildContext(student)).thenReturn(context);

        // With duplicates = true
        String mockInvalidResponse = createMockGeminiResponseJson(15, 15, 10, 5, 5, true);
        String mockPayload = buildGeminiCandidatesPayload(mockInvalidResponse);

        when(restTemplate.postForEntity(anyString(), any(), eq(String.class)))
                .thenReturn(new ResponseEntity<>(mockPayload, HttpStatus.OK));

        assertThrows(QuestionPoolGenerationException.class, () -> {
            questionPoolGenerationService.generateQuestionPool(student, InterviewDifficulty.MEDIUM);
        });
    }

    private String buildGeminiCandidatesPayload(String text) {
        String escaped = text.replace("\\", "\\\\").replace("\"", "\\\"");
        return "{\n" +
                "  \"candidates\": [{\n" +
                "    \"content\": {\n" +
                "      \"parts\": [{\n" +
                "        \"text\": \"" + escaped + "\"\n" +
                "      }]\n" +
                "    }\n" +
                "  }]\n" +
                "}";
    }

    private String createMockGeminiResponseJson(int projects, int technical, int scenario, int behavioral, int hr, boolean duplicate) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"questions\": [");

        List<String> entries = new ArrayList<>();

        for (int i = 1; i <= projects; i++) {
            String questionText = "Project Q" + i;
            if (duplicate && i == 2) {
                questionText = "Project Q1"; // duplicate of Q1
            }
            entries.add(createMockQuestionJson(questionText, "PROJECT"));
        }
        for (int i = 1; i <= technical; i++) {
            entries.add(createMockQuestionJson("Technical Q" + i, "TECHNICAL"));
        }
        for (int i = 1; i <= scenario; i++) {
            entries.add(createMockQuestionJson("Scenario Q" + i, "SCENARIO"));
        }
        for (int i = 1; i <= behavioral; i++) {
            entries.add(createMockQuestionJson("Behavioral Q" + i, "BEHAVIORAL"));
        }
        for (int i = 1; i <= hr; i++) {
            entries.add(createMockQuestionJson("HR Q" + i, "HR"));
        }

        sb.append(String.join(",", entries));
        sb.append("]}");

        return sb.toString();
    }

    private String createMockQuestionJson(String text, String category) {
        return "{" +
                "\"question\": \"" + text + " with at least fifteen characters long question text\"," +
                "\"idealAnswer\": \"This is an ideal answer that is also at least fifteen characters long\"," +
                "\"keyPoints\": [\"Point 1\", \"Point 2\"]," +
                "\"topic\": \"Mock Topic\"," +
                "\"category\": \"" + category + "\"," +
                "\"difficulty\": \"MEDIUM\"" +
                "}";
    }
}
