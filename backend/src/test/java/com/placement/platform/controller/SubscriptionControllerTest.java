package com.placement.platform.controller;

import com.placement.platform.premium.controller.SubscriptionController;
import com.placement.platform.premium.dto.SubscriptionResponseDto;
import com.placement.platform.premium.enums.SubscriptionStatus;
import com.placement.platform.premium.enums.SubscriptionType;
import com.placement.platform.premium.service.SubscriptionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = SubscriptionController.class)
@AutoConfigureMockMvc(addFilters = false)
public class SubscriptionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SubscriptionService subscriptionService;

    @MockBean
    private com.placement.platform.security.JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private org.springframework.security.core.userdetails.UserDetailsService userDetailsService;

    private SubscriptionResponseDto premiumResponseDto;
    private SubscriptionResponseDto freeResponseDto;

    @BeforeEach
    void setUp() {
        premiumResponseDto = new SubscriptionResponseDto(
                10L, 1L, "user@gmail.com", SubscriptionType.PREMIUM, SubscriptionStatus.ACTIVE,
                LocalDateTime.now(), LocalDateTime.now().plusDays(30), LocalDateTime.now(), LocalDateTime.now()
        );

        freeResponseDto = new SubscriptionResponseDto(
                10L, 1L, "user@gmail.com", SubscriptionType.FREE, SubscriptionStatus.ACTIVE,
                LocalDateTime.now(), null, LocalDateTime.now(), LocalDateTime.now()
        );
    }

    @Test
    void upgrade_Success() throws Exception {
        when(subscriptionService.upgradeSubscription(eq(1L), eq(SubscriptionType.PREMIUM), anyInt()))
                .thenReturn(premiumResponseDto);

        mockMvc.perform(post("/api/subscription/upgrade/1")
                        .param("type", "PREMIUM")
                        .param("durationDays", "30"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.subscriptionType").value("PREMIUM"))
                .andExpect(jsonPath("$.subscriptionStatus").value("ACTIVE"));
    }

    @Test
    void downgrade_Success() throws Exception {
        when(subscriptionService.downgradeSubscription(1L)).thenReturn(freeResponseDto);

        mockMvc.perform(post("/api/subscription/downgrade/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.subscriptionType").value("FREE"))
                .andExpect(jsonPath("$.subscriptionStatus").value("ACTIVE"));
    }

    @Test
    void getSubscriptionByUserId_Success() throws Exception {
        when(subscriptionService.getSubscriptionDtoByUserId(1L)).thenReturn(premiumResponseDto);

        mockMvc.perform(get("/api/subscription/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.subscriptionType").value("PREMIUM"));
    }
}
