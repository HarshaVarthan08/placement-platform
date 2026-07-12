package com.placement.platform.service;

import com.placement.platform.entity.User;
import com.placement.platform.premium.dto.FeatureAvailability;
import com.placement.platform.premium.dto.PremiumAccessResult;
import com.placement.platform.premium.entity.Subscription;
import com.placement.platform.premium.enums.FeatureStatus;
import com.placement.platform.premium.enums.PremiumFeature;
import com.placement.platform.premium.enums.SubscriptionStatus;
import com.placement.platform.premium.enums.SubscriptionType;
import com.placement.platform.premium.service.FeatureAvailabilityService;
import com.placement.platform.premium.service.PremiumAccessServiceImpl;
import com.placement.platform.premium.service.SubscriptionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PremiumAccessServiceTest {

    @Mock
    private SubscriptionService subscriptionService;

    @Mock
    private FeatureAvailabilityService availabilityService;

    @InjectMocks
    private PremiumAccessServiceImpl premiumAccessService;

    private User user;
    private Subscription freeSubscription;
    private Subscription premiumSubscription;
    private Subscription expiredSubscription;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("user@gmail.com");

        freeSubscription = new Subscription(user, SubscriptionType.FREE, SubscriptionStatus.ACTIVE, LocalDateTime.now(), null);
        premiumSubscription = new Subscription(user, SubscriptionType.PREMIUM, SubscriptionStatus.ACTIVE, LocalDateTime.now(), LocalDateTime.now().plusDays(30));
        expiredSubscription = new Subscription(user, SubscriptionType.PREMIUM, SubscriptionStatus.EXPIRED, LocalDateTime.now().minusDays(30), LocalDateTime.now().minusDays(1));
    }

    @Test
    void checkAccess_freeUser_deniedPremiumRequired() {
        when(subscriptionService.getActiveSubscription(user)).thenReturn(freeSubscription);
        when(availabilityService.getAvailability(PremiumFeature.AI_RESUME_OPTIMIZER))
                .thenReturn(new FeatureAvailability(PremiumFeature.AI_RESUME_OPTIMIZER, false, FeatureStatus.AVAILABLE, "Q4 2026"));

        PremiumAccessResult result = premiumAccessService.checkAccess(user, PremiumFeature.AI_RESUME_OPTIMIZER);

        assertFalse(result.allowed());
        assertEquals("PREMIUM_REQUIRED", result.reason());
        assertEquals(SubscriptionType.FREE, result.subscriptionType());
        assertEquals(FeatureStatus.AVAILABLE, result.featureStatus());
    }

    @Test
    void checkAccess_premiumUser_featureComingSoon_denied() {
        when(subscriptionService.getActiveSubscription(user)).thenReturn(premiumSubscription);
        when(availabilityService.getAvailability(PremiumFeature.AI_RESUME_OPTIMIZER))
                .thenReturn(new FeatureAvailability(PremiumFeature.AI_RESUME_OPTIMIZER, false, FeatureStatus.COMING_SOON, "Q4 2026"));

        PremiumAccessResult result = premiumAccessService.checkAccess(user, PremiumFeature.AI_RESUME_OPTIMIZER);

        assertFalse(result.allowed());
        assertEquals("FEATURE_COMING_SOON", result.reason());
        assertEquals(SubscriptionType.PREMIUM, result.subscriptionType());
        assertEquals(FeatureStatus.COMING_SOON, result.featureStatus());
    }

    @Test
    void checkAccess_premiumUser_featureAvailable_allowed() {
        when(subscriptionService.getActiveSubscription(user)).thenReturn(premiumSubscription);
        when(availabilityService.getAvailability(PremiumFeature.AI_RESUME_OPTIMIZER))
                .thenReturn(new FeatureAvailability(PremiumFeature.AI_RESUME_OPTIMIZER, true, FeatureStatus.AVAILABLE, "Q4 2026"));

        PremiumAccessResult result = premiumAccessService.checkAccess(user, PremiumFeature.AI_RESUME_OPTIMIZER);

        assertTrue(result.allowed());
        assertEquals("ACCESS_GRANTED", result.reason());
        assertEquals(SubscriptionType.PREMIUM, result.subscriptionType());
        assertEquals(FeatureStatus.AVAILABLE, result.featureStatus());
    }

    @Test
    void checkAccess_expiredSubscription_denied() {
        when(subscriptionService.getActiveSubscription(user)).thenReturn(expiredSubscription);
        when(availabilityService.getAvailability(PremiumFeature.AI_RESUME_OPTIMIZER))
                .thenReturn(new FeatureAvailability(PremiumFeature.AI_RESUME_OPTIMIZER, true, FeatureStatus.AVAILABLE, "Q4 2026"));

        PremiumAccessResult result = premiumAccessService.checkAccess(user, PremiumFeature.AI_RESUME_OPTIMIZER);

        assertFalse(result.allowed());
        assertEquals("SUBSCRIPTION_INACTIVE", result.reason());
        assertEquals(SubscriptionType.PREMIUM, result.subscriptionType());
        assertEquals(FeatureStatus.AVAILABLE, result.featureStatus());
    }

    @Test
    void isPremium_freeUser_returnsFalse() {
        when(subscriptionService.getActiveSubscription(user)).thenReturn(freeSubscription);

        boolean result = premiumAccessService.isPremium(user);

        assertFalse(result);
    }

    @Test
    void isPremium_premiumUser_returnsTrue() {
        when(subscriptionService.getActiveSubscription(user)).thenReturn(premiumSubscription);

        boolean result = premiumAccessService.isPremium(user);

        assertTrue(result);
    }
}
