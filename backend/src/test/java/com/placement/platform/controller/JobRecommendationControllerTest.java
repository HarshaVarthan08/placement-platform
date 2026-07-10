package com.placement.platform.controller;

import com.placement.platform.entity.User;
import com.placement.platform.job.dto.*;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
    private RecommendationLifecycleService lifecycleService;

    @MockBean
    private ApplicationTrackingService applicationTrackingService;

    @MockBean
    private RecommendationAnalyticsService analyticsService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private JobRepository jobRepository;

    @MockBean
    private JobRecommendationRepository jobRecommendationRepository;

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
        recommendation.setRecommendationStatus(RecommendationStatus.NEW);
        recommendation.setGeneratedAt(LocalDateTime.of(2026, 7, 6, 12, 0));
        recommendation.setGenerationId("GEN_UUID");
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
    void testMarkViewed_Success() throws Exception {
        recommendation.setRecommendationStatus(RecommendationStatus.VIEWED);
        recommendation.setViewCount(1);
        recommendation.setViewedAt(LocalDateTime.now());

        when(lifecycleService.markViewed(100L)).thenReturn(recommendation);
        when(jobRepository.findById(10L)).thenReturn(Optional.of(job));

        mockMvc.perform(post("/api/jobs/recommendations/100/view")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.recommendationStatus").value("VIEWED"))
                .andExpect(jsonPath("$.viewCount").value(1));
    }

    @Test
    @WithMockUser(username = "student@example.com", authorities = "ROLE_STUDENT")
    void testSaveRecommendation_Success() throws Exception {
        recommendation.setRecommendationStatus(RecommendationStatus.SAVED);
        recommendation.setSavedAt(LocalDateTime.now());

        when(lifecycleService.saveRecommendation(100L)).thenReturn(recommendation);
        when(jobRepository.findById(10L)).thenReturn(Optional.of(job));

        mockMvc.perform(post("/api/jobs/recommendations/100/save")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.recommendationStatus").value("SAVED"));
    }

    @Test
    @WithMockUser(username = "student@example.com", authorities = "ROLE_STUDENT")
    void testUnsaveRecommendation_Success() throws Exception {
        recommendation.setRecommendationStatus(RecommendationStatus.VIEWED);

        when(lifecycleService.unsaveRecommendation(100L)).thenReturn(recommendation);
        when(jobRepository.findById(10L)).thenReturn(Optional.of(job));

        mockMvc.perform(post("/api/jobs/recommendations/100/unsave")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.recommendationStatus").value("VIEWED"));
    }

    @Test
    @WithMockUser(username = "student@example.com", authorities = "ROLE_STUDENT")
    void testHideRecommendation_Success() throws Exception {
        recommendation.setRecommendationStatus(RecommendationStatus.HIDDEN);
        recommendation.setHidden(true);

        when(lifecycleService.hideRecommendation(100L)).thenReturn(recommendation);
        when(jobRepository.findById(10L)).thenReturn(Optional.of(job));

        mockMvc.perform(post("/api/jobs/recommendations/100/hide")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.recommendationStatus").value("HIDDEN"))
                .andExpect(jsonPath("$.hidden").value(true));
    }

    @Test
    @WithMockUser(username = "student@example.com", authorities = "ROLE_STUDENT")
    void testArchiveRecommendation_Success() throws Exception {
        recommendation.setRecommendationStatus(RecommendationStatus.ARCHIVED);

        when(lifecycleService.archiveRecommendation(100L)).thenReturn(recommendation);
        when(jobRepository.findById(10L)).thenReturn(Optional.of(job));

        mockMvc.perform(post("/api/jobs/recommendations/100/archive")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.recommendationStatus").value("ARCHIVED"));
    }

    @Test
    @WithMockUser(username = "student@example.com", authorities = "ROLE_STUDENT")
    void testSubmitFeedback_Success() throws Exception {
        recommendation.setFeedbackType(RecommendationFeedbackType.USEFUL);
        recommendation.setFeedbackNotes("Excellent job match!");

        when(lifecycleService.submitFeedback(eq(100L), eq(RecommendationFeedbackType.USEFUL), eq("Excellent job match!")))
                .thenReturn(recommendation);
        when(jobRepository.findById(10L)).thenReturn(Optional.of(job));

        mockMvc.perform(post("/api/jobs/recommendations/100/feedback")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"feedbackType\":\"USEFUL\",\"feedbackNotes\":\"Excellent job match!\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.feedbackType").value("USEFUL"))
                .andExpect(jsonPath("$.feedbackNotes").value("Excellent job match!"));
    }

    @Test
    @WithMockUser(username = "student@example.com", authorities = "ROLE_STUDENT")
    void testApplyForRecommendation_Success() throws Exception {
        recommendation.setRecommendationStatus(RecommendationStatus.APPLIED);
        recommendation.setApplicationStatus(ApplicationStatus.APPLIED);
        recommendation.setApplicationUrl("http://google.com/careers");

        when(applicationTrackingService.applyForRecommendation(eq(100L), eq("http://google.com/careers"), eq("REF1"), eq("Notes")))
                .thenReturn(recommendation);
        when(jobRepository.findById(10L)).thenReturn(Optional.of(job));

        mockMvc.perform(post("/api/jobs/recommendations/100/apply")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"applicationUrl\":\"http://google.com/careers\",\"applicationReference\":\"REF1\",\"applicationNotes\":\"Notes\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.applicationStatus").value("APPLIED"))
                .andExpect(jsonPath("$.applicationUrl").value("http://google.com/careers"));
    }

    @Test
    @WithMockUser(username = "student@example.com", authorities = "ROLE_STUDENT")
    void testUpdateApplicationStage_Success() throws Exception {
        recommendation.setApplicationStatus(ApplicationStatus.TECHNICAL);

        when(applicationTrackingService.updateApplicationStage(eq(100L), eq(ApplicationStatus.TECHNICAL), eq("Stage update notes")))
                .thenReturn(recommendation);
        when(jobRepository.findById(10L)).thenReturn(Optional.of(job));

        mockMvc.perform(put("/api/jobs/recommendations/100/application")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"applicationStatus\":\"TECHNICAL\",\"applicationNotes\":\"Stage update notes\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.applicationStatus").value("TECHNICAL"));
    }

    @Test
    @WithMockUser(username = "student@example.com", authorities = "ROLE_STUDENT")
    void testGetAppliedRecommendations_Success() throws Exception {
        when(userRepository.findByEmail("student@example.com")).thenReturn(Optional.of(studentUser));
        when(applicationTrackingService.getAppliedRecommendations(eq(studentUser), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(recommendation)));
        when(jobRepository.findAllById(List.of(10L))).thenReturn(List.of(job));

        mockMvc.perform(get("/api/jobs/recommendations/applied")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].recommendationId").value(100))
                .andExpect(jsonPath("$.content[0].jobId").value(10));
    }

    @Test
    @WithMockUser(username = "student@example.com", authorities = "ROLE_STUDENT")
    void testGetSavedRecommendations_Success() throws Exception {
        recommendation.setRecommendationStatus(RecommendationStatus.SAVED);
        recommendation.setSavedAt(LocalDateTime.of(2026, 7, 6, 12, 30));

        when(userRepository.findByEmail("student@example.com")).thenReturn(Optional.of(studentUser));
        when(jobRecommendationRepository.findByUserIdAndRecommendationStatusAndHiddenFalse(eq(1L), eq(RecommendationStatus.SAVED), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(recommendation)));
        when(jobRepository.findAllById(List.of(10L))).thenReturn(List.of(job));

        mockMvc.perform(get("/api/jobs/recommendations/saved")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].recommendationId").value(100))
                .andExpect(jsonPath("$.content[0].company").value("Google"));
    }

    @Test
    @WithMockUser(username = "student@example.com", authorities = "ROLE_STUDENT")
    void testGetAnalytics_Success() throws Exception {
        RecommendationAnalyticsResponseDto analytics = new RecommendationAnalyticsResponseDto();
        analytics.setTotalRecommendations(5L);
        analytics.setAverageMatchScore(85.0);

        when(userRepository.findByEmail("student@example.com")).thenReturn(Optional.of(studentUser));
        when(analyticsService.getAnalytics(eq(studentUser), anyBoolean())).thenReturn(analytics);

        mockMvc.perform(get("/api/jobs/recommendations/analytics?includeHistory=true")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalRecommendations").value(5))
                .andExpect(jsonPath("$.averageMatchScore").value(85.0));
    }

    @Test
    @WithMockUser(username = "student@example.com", authorities = "ROLE_STUDENT")
    void testGetHistory_Success() throws Exception {
        when(userRepository.findByEmail("student@example.com")).thenReturn(Optional.of(studentUser));
        when(jobRecommendationRepository.findByUserId(1L)).thenReturn(List.of(recommendation));
        when(jobRepository.findAllById(anyList())).thenReturn(List.of(job));

        mockMvc.perform(get("/api/jobs/recommendations/history")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].generationId").value("GEN_UUID"))
                .andExpect(jsonPath("$[0].recommendationsCount").value(1));
    }

    @Test
    @WithMockUser(username = "student@example.com", authorities = "ROLE_STUDENT")
    void testGetTimeline_Success() throws Exception {
        when(recommendationService.getRecommendationDetails(100L)).thenReturn(recommendation);
        when(jobRepository.findById(10L)).thenReturn(Optional.of(job));

        mockMvc.perform(get("/api/jobs/recommendations/100/timeline")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.recommendationId").value(100))
                .andExpect(jsonPath("$.company").value("Google"))
                .andExpect(jsonPath("$.events[0].eventType").value("GENERATED"));
    }
}
