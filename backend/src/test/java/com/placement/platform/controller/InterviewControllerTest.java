package com.placement.platform.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.placement.platform.dto.InterviewAvailabilityResponseDto;
import com.placement.platform.dto.InterviewSessionResponseDto;
import com.placement.platform.dto.StartInterviewRequestDto;
import com.placement.platform.entity.InterviewDifficulty;
import com.placement.platform.entity.InterviewMode;
import com.placement.platform.entity.InterviewStatus;
import com.placement.platform.service.InterviewService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
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

    // We must mock security-related beans that are needed by SecurityConfig during WebMvcTest loading
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
        // Mode is null (violates @NotNull)
        StartInterviewRequestDto request = new StartInterviewRequestDto(null, InterviewDifficulty.MEDIUM);

        mockMvc.perform(post("/api/interview/start")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void startInterview_OptionalDifficulty_ShouldReturnSuccess() throws Exception {
        // Difficulty is null (should be allowed and defaulted by service)
        StartInterviewRequestDto request = new StartInterviewRequestDto(InterviewMode.LEARNING, null);

        LocalDateTime startedAt = LocalDateTime.of(2026, 6, 28, 14, 0, 0);
        InterviewSessionResponseDto response = new InterviewSessionResponseDto(
                102L, InterviewMode.LEARNING, InterviewStatus.IN_PROGRESS, startedAt
        );

        when(interviewService.startInterview(any(StartInterviewRequestDto.class))).thenReturn(response);

        mockMvc.perform(post("/api/interview/start")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sessionId").value(102))
                .andExpect(jsonPath("$.mode").value("LEARNING"));
    }
}
