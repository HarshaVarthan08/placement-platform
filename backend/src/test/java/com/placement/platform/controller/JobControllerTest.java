package com.placement.platform.controller;

import com.placement.platform.job.controller.JobController;
import com.placement.platform.job.dto.SyncReportDto;
import com.placement.platform.job.entity.JobSourceType;
import com.placement.platform.job.entity.SyncStatus;
import com.placement.platform.job.repository.JobRepository;
import com.placement.platform.job.service.JobSynchronizationManager;
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
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = JobController.class)
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
public class JobControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JobSynchronizationManager syncManager;

    @MockBean
    private JobRepository jobRepository;

    @MockBean
    private com.placement.platform.security.JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private org.springframework.security.core.userdetails.UserDetailsService userDetailsService;

    @MockBean
    private com.placement.platform.security.JwtService jwtService;

    @BeforeEach
    void setUp() throws Exception {
        // Setup JwtAuthenticationFilter to be a pass-through mock filter
        doAnswer(invocation -> {
            HttpServletRequest request = invocation.getArgument(0);
            HttpServletResponse response = invocation.getArgument(1);
            FilterChain chain = invocation.getArgument(2);
            chain.doFilter(request, response);
            return null;
        }).when(jwtAuthenticationFilter).doFilter(any(), any(), any());
    }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void testSyncJobs_Admin_ShouldReturnReport() throws Exception {
        SyncReportDto report = new SyncReportDto();
        report.setSource(JobSourceType.STATIC);
        report.setImportedJobsCount(50);
        report.setInsertedJobsCount(48);
        report.setUpdatedJobsCount(2);
        report.setStatus(SyncStatus.SUCCESS);

        when(syncManager.synchronizeAll()).thenReturn(Collections.singletonList(report));

        mockMvc.perform(post("/api/jobs/sync")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].source").value("STATIC"))
                .andExpect(jsonPath("$[0].importedJobsCount").value(50));
    }

    @Test
    @WithMockUser(authorities = "ROLE_STUDENT")
    void testSyncJobs_Student_ShouldReturnForbidden() throws Exception {
        mockMvc.perform(post("/api/jobs/sync")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void testSyncJobs_Unauthenticated_ShouldReturnForbidden() throws Exception {
        mockMvc.perform(post("/api/jobs/sync")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "ROLE_STUDENT")
    void testGetActiveJobs_Authenticated_ShouldReturnList() throws Exception {
        when(jobRepository.findActiveJobs()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/jobs")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testGetActiveJobs_Unauthenticated_ShouldReturnForbidden() throws Exception {
        mockMvc.perform(get("/api/jobs")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
}
