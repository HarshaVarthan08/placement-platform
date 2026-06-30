package com.placement.platform.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.placement.platform.dto.InterviewEvaluationResponseDto;
import com.placement.platform.dto.InterviewEvaluationStatusResponseDto;
import com.placement.platform.service.InterviewEvaluationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = InterviewEvaluationController.class)
@AutoConfigureMockMvc(addFilters = false)
public class InterviewEvaluationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InterviewEvaluationService interviewEvaluationService;

    @MockBean
    private com.placement.platform.security.JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private org.springframework.security.core.userdetails.UserDetailsService userDetailsService;

    @Test
    void evaluateInterview_ShouldReturnEvaluation() throws Exception {
        InterviewEvaluationResponseDto expectedResponse = new InterviewEvaluationResponseDto(
                1L, 10L, 85, 80, 90, 85, 85, 82, "GOOD", "HIRE", "Justification", "Summary", "Feedback",
                List.of("Java coding"), List.of("None"), List.of("concurrency"), List.of("Read books"),
                "gemini-2.5-flash", "COMPLETED", LocalDateTime.now(), List.of()
        );

        when(interviewEvaluationService.evaluateInterview(10L)).thenReturn(expectedResponse);

        mockMvc.perform(post("/api/interview/evaluate")
                        .param("sessionId", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.overallScore").value(85))
                .andExpect(jsonPath("$.performanceBand").value("GOOD"))
                .andExpect(jsonPath("$.verdict").value("HIRE"));

        verify(interviewEvaluationService, times(1)).evaluateInterview(10L);
    }

    @Test
    void evaluateInterview_NoSessionId_ShouldReturnEvaluation() throws Exception {
        InterviewEvaluationResponseDto expectedResponse = new InterviewEvaluationResponseDto(
                1L, 10L, 85, 80, 90, 85, 85, 82, "GOOD", "HIRE", "Justification", "Summary", "Feedback",
                List.of("Java coding"), List.of("None"), List.of("concurrency"), List.of("Read books"),
                "gemini-2.5-flash", "COMPLETED", LocalDateTime.now(), List.of()
        );

        when(interviewEvaluationService.evaluateInterview(null)).thenReturn(expectedResponse);

        mockMvc.perform(post("/api/interview/evaluate"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.overallScore").value(85));

        verify(interviewEvaluationService, times(1)).evaluateInterview(null);
    }

    @Test
    void getEvaluation_ShouldReturnCachedResult() throws Exception {
        InterviewEvaluationResponseDto expectedResponse = new InterviewEvaluationResponseDto(
                1L, 10L, 85, 80, 90, 85, 85, 82, "GOOD", "HIRE", "Justification", "Summary", "Feedback",
                List.of("Java coding"), List.of("None"), List.of("concurrency"), List.of("Read books"),
                "gemini-2.5-flash", "COMPLETED", LocalDateTime.now(), List.of()
        );

        when(interviewEvaluationService.getEvaluation(10L)).thenReturn(expectedResponse);

        mockMvc.perform(get("/api/interview/evaluation")
                        .param("sessionId", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.overallScore").value(85));

        verify(interviewEvaluationService, times(1)).getEvaluation(10L);
    }

    @Test
    void getEvaluationStatus_ShouldReturnStatus() throws Exception {
        InterviewEvaluationStatusResponseDto expectedResponse = new InterviewEvaluationStatusResponseDto(10L, "COMPLETED");

        when(interviewEvaluationService.getEvaluationStatus(10L)).thenReturn(expectedResponse);

        mockMvc.perform(get("/api/interview/evaluation/status")
                        .param("sessionId", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("COMPLETED"));

        verify(interviewEvaluationService, times(1)).getEvaluationStatus(10L);
    }
}
