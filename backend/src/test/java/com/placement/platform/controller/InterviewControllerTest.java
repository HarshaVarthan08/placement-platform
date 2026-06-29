package com.placement.platform.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.placement.platform.dto.*;
import com.placement.platform.entity.*;
import com.placement.platform.service.InterviewService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = InterviewController.class)
@AutoConfigureMockMvc(addFilters = false) // Bypasses security filters to test endpoints in isolation
public class InterviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private InterviewService interviewService;

    @MockBean
    private com.placement.platform.security.JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private org.springframework.security.core.userdetails.UserDetailsService userDetailsService;

    @Test
    void checkAvailability_ShouldReturnResponse() throws Exception {
        LocalDateTime nextAvailable = LocalDateTime.of(2026, 6, 28, 18, 0, 0);
        InterviewAvailabilityResponseDto response = new InterviewAvailabilityResponseDto(
                false, nextAvailable, 4, 30
            );

        when(interviewService.checkAvailability()).thenReturn(response);

        mockMvc.perform(get("/api/interview/availability"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.available").value(false))
                .andExpect(jsonPath("$.remainingHours").value(4))
                .andExpect(jsonPath("$.remainingMinutes").value(30));
    }

    @Test
    void startInterview_ValidRequest_ShouldReturnSessionResponse() throws Exception {
        StartInterviewRequestDto request = new StartInterviewRequestDto(InterviewMode.MOCK, InterviewDifficulty.MEDIUM);
        
        LocalDateTime startedAt = LocalDateTime.of(2026, 6, 28, 14, 0, 0);
        InterviewSessionResponseDto response = new InterviewSessionResponseDto(
                101L, InterviewMode.MOCK, InterviewStatus.IN_PROGRESS, startedAt
            );

        when(interviewService.startInterview(any(StartInterviewRequestDto.class))).thenReturn(response);

        mockMvc.perform(post("/api/interview/start")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sessionId").value(101))
                .andExpect(jsonPath("$.mode").value("MOCK"))
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));
    }

    @Test
    void startInterview_InvalidRequest_MissingMode_ShouldReturnBadRequest() throws Exception {
        StartInterviewRequestDto request = new StartInterviewRequestDto(null, InterviewDifficulty.MEDIUM);

        mockMvc.perform(post("/api/interview/start")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getCurrentQuestion_ShouldReturnQuestion() throws Exception {
        InterviewQuestionResponseDto questionResponse = new InterviewQuestionResponseDto(
                200L,
                "Explain inheritance.",
                "OOP",
                InterviewDifficulty.MEDIUM,
                QuestionCategory.TECHNICAL,
                1,
                QuestionSource.PERSONALIZED,
                SessionQuestionStatus.CURRENT
            );

        when(interviewService.getCurrentQuestion()).thenReturn(questionResponse);

        mockMvc.perform(get("/api/interview/current-question"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sessionQuestionId").value(200))
                .andExpect(jsonPath("$.question").value("Explain inheritance."))
                .andExpect(jsonPath("$.status").value("CURRENT"));
    }

    @Test
    void answerCurrentQuestion_ShouldReturnOk() throws Exception {
        SubmitAnswerRequestDto request = new SubmitAnswerRequestDto(AnswerType.TEXT, "Inheritance is...", null, 45);

        doNothing().when(interviewService).answerCurrentQuestion(any(SubmitAnswerRequestDto.class));

        mockMvc.perform(post("/api/interview/answer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(interviewService, times(1)).answerCurrentQuestion(any(SubmitAnswerRequestDto.class));
    }

    @Test
    void goToNextQuestion_ShouldReturnProgress() throws Exception {
        InterviewProgressResponseDto progressResponse = new InterviewProgressResponseDto(
                101L,
                InterviewMode.MOCK,
                InterviewStatus.IN_PROGRESS,
                15,
                1,
                1,
                14,
                Collections.emptyList()
            );

        when(interviewService.goToNextQuestion()).thenReturn(progressResponse);

        mockMvc.perform(post("/api/interview/next"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentQuestionIndex").value(1))
                .andExpect(jsonPath("$.remainingQuestions").value(14));
    }

    @Test
    void getProgress_ShouldReturnProgress() throws Exception {
        InterviewProgressResponseDto progressResponse = new InterviewProgressResponseDto(
                101L,
                InterviewMode.MOCK,
                InterviewStatus.IN_PROGRESS,
                15,
                0,
                0,
                15,
                Collections.emptyList()
            );

        when(interviewService.getProgress()).thenReturn(progressResponse);

        mockMvc.perform(get("/api/interview/progress"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalQuestions").value(15))
                .andExpect(jsonPath("$.remainingQuestions").value(15));
    }

    @Test
    void completeInterview_ShouldReturnSession() throws Exception {
        LocalDateTime completedAt = LocalDateTime.now();
        InterviewSessionResponseDto response = new InterviewSessionResponseDto(
                101L,
                InterviewMode.MOCK,
                InterviewStatus.COMPLETED,
                completedAt
            );

        when(interviewService.completeInterview()).thenReturn(response);

        mockMvc.perform(post("/api/interview/complete"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("COMPLETED"));
    }
}
