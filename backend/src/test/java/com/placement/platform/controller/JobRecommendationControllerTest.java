package com.placement.platform.controller;

import com.placement.platform.entity.User;
import com.placement.platform.job.controller.JobController;
import com.placement.platform.job.dto.RecommendationSummaryDto;
import com.placement.platform.job.entity.Job;
import com.placement.platform.job.recommendation.*;
import com.placement.platform.repository.UserRepository;
import com.placement.platform.job.repository.JobRepository;
import com.placement.platform.security.SecurityConfig;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = JobRecommendationController.class)
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
public class JobRecommendationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecommendationService recommendationService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private JobRepository jobRepository;

    @MockBean
    private com.placement.platform.security.JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private org.springframework.security.core.userdetails.UserDetailsService userDetailsService;

    @MockBean
    private com.placement.platform.security.JwtService jwtService;

    private User studentUser;
    private Job job;
    private JobRecommendation recommendation;

    @BeforeEach
    void setUp() throws Exception {
        // Pass-through Security Filter mock
        doAnswer(invocation -> {
            HttpServletRequest request = invocation.getArgument(0);
            HttpServletResponse response = invocation.getArgument(1);
            FilterChain chain = invocation.getArgument(2);
            chain.doFilter(request, response);
            return null;
        }).when(jwtAuthenticationFilter).doFilter(any(), any(), any());

        studentUser = new User();
        studentUser.setId(1L);
        studentUser.setEmail("student@example.com");

        job = new Job();
        job.setId(10L);
        job.setCompany("Google");
        job.setTitle("SWE");

        recommendation = new JobRecommendation();
        recommendation.setId(100L);
        recommendation.setUserId(1L);
        recommendation.setJobId(10L);
        recommendation.setMatchScore(95);
        recommendation.setConfidenceScore(100);
        recommendation.setRecommendationLevel(RecommendationLevel.EXCELLENT);
        recommendation.setRecommendationPriority(RecommendationPriority.HIGH);
        recommendation.setRecommendationAction(RecommendationAction.APPLY_NOW);
    }

    @Test
    @WithMockUser(username = "student@example.com", authorities = "ROLE_STUDENT")
    void testGenerateRecommendations_Success() throws Exception {
        when(userRepository.findByEmail("student@example.com")).thenReturn(Optional.of(studentUser));
        when(recommendationService.generateRecommendations(studentUser)).thenReturn(List.of(recommendation));
        when(jobRepository.findAllById(List.of(10L))).thenReturn(List.of(job));

        mockMvc.perform(post("/api/jobs/recommendations/generate")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(100))
                .andExpect(jsonPath("$[0].matchScore").value(95))
                .andExpect(jsonPath("$[0].recommendationLevel").value("EXCELLENT"))
                .andExpect(jsonPath("$[0].job.company").value("Google"));
    }

    @Test
    @WithMockUser(username = "student@example.com", authorities = "ROLE_STUDENT")
    void testGetRecommendations_Success() throws Exception {
        when(userRepository.findByEmail("student@example.com")).thenReturn(Optional.of(studentUser));
        when(recommendationService.getRecommendations(eq(studentUser), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(recommendation)));
        when(jobRepository.findAllById(List.of(10L))).thenReturn(List.of(job));

        mockMvc.perform(get("/api/jobs/recommendations")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(100))
                .andExpect(jsonPath("$.content[0].job.title").value("SWE"));
    }

    @Test
    @WithMockUser(username = "student@example.com", authorities = "ROLE_STUDENT")
    void testGetRecommendationDetails_Success() throws Exception {
        when(recommendationService.getRecommendationDetails(100L)).thenReturn(recommendation);
        when(jobRepository.findById(10L)).thenReturn(Optional.of(job));

        mockMvc.perform(get("/api/jobs/recommendations/100")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.job.company").value("Google"));
    }

    @Test
    @WithMockUser(username = "student@example.com", authorities = "ROLE_STUDENT")
    void testGetRecommendationSummary_Success() throws Exception {
        RecommendationSummaryDto summary = new RecommendationSummaryDto(5, 2, 2, 85.0, 95.0, "Google");

        when(userRepository.findByEmail("student@example.com")).thenReturn(Optional.of(studentUser));
        when(recommendationService.getRecommendationSummary(studentUser)).thenReturn(summary);

        mockMvc.perform(get("/api/jobs/recommendations/summary")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalRecommendations").value(5))
                .andExpect(jsonPath("$.excellentMatches").value(2))
                .andExpect(jsonPath("$.averageMatchScore").value(85.0))
                .andExpect(jsonPath("$.topRecommendedCompany").value("Google"));
    }
}
