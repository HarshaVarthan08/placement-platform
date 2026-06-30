package com.placement.platform.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.placement.platform.dto.dashboard.DashboardResponseDto;
import com.placement.platform.dto.dashboard.PlacementReadinessCard;
import com.placement.platform.dto.dashboard.ResumeCard;
import com.placement.platform.entity.User;
import com.placement.platform.repository.UserRepository;
import com.placement.platform.service.dashboard.PlacementIntelligenceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = DashboardController.class)
@AutoConfigureMockMvc(addFilters = false)
public class DashboardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlacementIntelligenceService placementIntelligenceService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private com.placement.platform.security.JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private org.springframework.security.core.userdetails.UserDetailsService userDetailsService;

    private User student;

    @BeforeEach
    void setUp() {
        student = new User();
        student.setId(1L);
        student.setEmail("harsha@gmail.com");

        // Mock Security Context
        Authentication authentication = mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("harsha@gmail.com");

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByEmail("harsha@gmail.com")).thenReturn(Optional.of(student));
    }

    @Test
    void getDashboard_Success() throws Exception {
        PlacementReadinessCard readinessCard = new PlacementReadinessCard(
                "AVAILABLE", 85, "GOOD", null, "Resume and Profile are excellent", 5, "+6.2%", LocalDateTime.now()
        );
        ResumeCard resumeCard = new ResumeCard(
                "AVAILABLE", 82, 1, true, true, LocalDateTime.now(), LocalDateTime.now(), 0, "N/A", LocalDateTime.now()
        );

        DashboardResponseDto responseDto = new DashboardResponseDto(
                LocalDateTime.now(),
                1,
                readinessCard,
                resumeCard,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );

        when(placementIntelligenceService.getDashboardResponse(any(User.class))).thenReturn(responseDto);

        mockMvc.perform(get("/api/dashboard"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dashboardVersion").value(1))
                .andExpect(jsonPath("$.placementReadiness.readinessScore").value(85))
                .andExpect(jsonPath("$.placementReadiness.readinessBand").value("GOOD"))
                .andExpect(jsonPath("$.resume.latestAts").value(82));
    }
}
