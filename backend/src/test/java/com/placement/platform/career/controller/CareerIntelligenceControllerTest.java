package com.placement.platform.career.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.placement.platform.career.intelligence.CareerConfidenceBand;
import com.placement.platform.career.intelligence.CareerIntelligenceProfile;
import com.placement.platform.career.intelligence.PreparationDifficulty;
import com.placement.platform.career.intelligence.ProfileHealth;
import com.placement.platform.career.service.CareerIntelligenceService;
import com.placement.platform.entity.User;
import com.placement.platform.repository.UserRepository;
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
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CareerIntelligenceController.class)
@AutoConfigureMockMvc(addFilters = false)
public class CareerIntelligenceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CareerIntelligenceService careerIntelligenceService;

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

        Authentication authentication = mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("harsha@gmail.com");

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByEmail("harsha@gmail.com")).thenReturn(Optional.of(student));
    }

    @Test
    void getCareerIntelligenceProfile_Success() throws Exception {
        CareerIntelligenceProfile mockProfile = new CareerIntelligenceProfile(
                1L, 25L, 10L, "Microsoft", "Software Engineer",
                82, 91, 88, 79, 84, 87, CareerConfidenceBand.VERY_HIGH,
                List.of("Java", "Spring Boot"), List.of("Docker"),
                "Docker", "Required by target role.", null, List.of(),
                PreparationDifficulty.MEDIUM, 4, ProfileHealth.COMPLETE,
                null, null, null,
                1, "1.0.0", "CareerIntelligenceEngine", LocalDateTime.now()
        );

        when(careerIntelligenceService.getProfile(eq(25L), any(User.class))).thenReturn(mockProfile);

        mockMvc.perform(get("/api/career/intelligence/25"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.recommendationId").value(25))
                .andExpect(jsonPath("$.company").value("Microsoft"))
                .andExpect(jsonPath("$.jobTitle").value("Software Engineer"))
                .andExpect(jsonPath("$.careerConfidenceScore").value(87))
                .andExpect(jsonPath("$.confidenceBand").value("VERY_HIGH"))
                .andExpect(jsonPath("$.preparationDifficulty").value("MEDIUM"))
                .andExpect(jsonPath("$.estimatedPreparationWeeks").value(4))
                .andExpect(jsonPath("$.profileHealth").value("COMPLETE"));
    }
}
