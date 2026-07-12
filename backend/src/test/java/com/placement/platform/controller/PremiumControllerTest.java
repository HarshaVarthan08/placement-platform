package com.placement.platform.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.placement.platform.entity.User;
import com.placement.platform.premium.config.PremiumProperties;
import com.placement.platform.premium.controller.PremiumController;
import com.placement.platform.premium.dto.FeatureAvailability;
import com.placement.platform.premium.dto.WaitlistRequestDto;
import com.placement.platform.premium.enums.FeatureStatus;
import com.placement.platform.premium.enums.PremiumFeature;
import com.placement.platform.premium.repository.PremiumWaitlistRepository;
import com.placement.platform.premium.service.FeatureAvailabilityService;
import com.placement.platform.premium.service.PremiumAccessService;
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

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PremiumController.class)
@AutoConfigureMockMvc(addFilters = false)
public class PremiumControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PremiumAccessService premiumAccessService;

    @MockBean
    private FeatureAvailabilityService availabilityService;

    @MockBean
    private PremiumWaitlistRepository waitlistRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PremiumProperties premiumProperties;

    @MockBean
    private com.placement.platform.security.JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private org.springframework.security.core.userdetails.UserDetailsService userDetailsService;

    private User user;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("user@gmail.com");

        Authentication authentication = mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("user@gmail.com");

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByEmail("user@gmail.com")).thenReturn(Optional.of(user));
    }

    @Test
    void getCatalog_Success() throws Exception {
        when(availabilityService.getAvailability(any(PremiumFeature.class)))
                .thenReturn(new FeatureAvailability(PremiumFeature.AI_RESUME_OPTIMIZER, false, FeatureStatus.COMING_SOON, "Q4 2026"));

        mockMvc.perform(get("/api/premium/catalog"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].code").value("AI_RESUME_OPTIMIZER"))
                .andExpect(jsonPath("$[0].status").value("COMING_SOON"));
    }

    @Test
    void joinWaitlist_Success() throws Exception {
        when(premiumProperties.isWaitlistEnabled()).thenReturn(true);
        when(waitlistRepository.existsByUserIdAndFeature(eq(user.getId()), any(PremiumFeature.class))).thenReturn(false);

        WaitlistRequestDto request = new WaitlistRequestDto("AI_CAREER_COACH", "Dashboard");

        mockMvc.perform(post("/api/premium/waitlist")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Successfully registered for the AI Career Coach waitlist."));

        verify(waitlistRepository, times(1)).save(any());
    }

    @Test
    void joinWaitlist_AlreadyJoined() throws Exception {
        when(premiumProperties.isWaitlistEnabled()).thenReturn(true);
        when(waitlistRepository.existsByUserIdAndFeature(eq(user.getId()), any(PremiumFeature.class))).thenReturn(true);

        WaitlistRequestDto request = new WaitlistRequestDto("AI_CAREER_COACH", "Dashboard");

        mockMvc.perform(post("/api/premium/waitlist")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("You are already registered on the waitlist for AI Career Coach."));

        verify(waitlistRepository, never()).save(any());
    }

    @Test
    void placeholderEndpoints_FreeUser_Forbidden() throws Exception {
        when(premiumAccessService.isPremium(user)).thenReturn(false);

        mockMvc.perform(post("/api/premium/resume-optimizer"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.status").value("COMING_SOON"))
                .andExpect(jsonPath("$.premiumRequired").value(true));
    }

    @Test
    void placeholderEndpoints_PremiumUser_NotImplemented() throws Exception {
        when(premiumAccessService.isPremium(user)).thenReturn(true);

        mockMvc.perform(post("/api/premium/resume-optimizer"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.status").value("NOT_IMPLEMENTED"));
    }
}
