package com.placement.platform.service;

import com.placement.platform.entity.User;
import com.placement.platform.exception.UserNotFoundException;
import com.placement.platform.premium.dto.SubscriptionResponseDto;
import com.placement.platform.premium.entity.Subscription;
import com.placement.platform.premium.enums.SubscriptionStatus;
import com.placement.platform.premium.enums.SubscriptionType;
import com.placement.platform.premium.repository.SubscriptionRepository;
import com.placement.platform.premium.service.SubscriptionServiceImpl;
import com.placement.platform.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SubscriptionServiceTest {

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SubscriptionServiceImpl subscriptionService;

    private User user;
    private Subscription subscription;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@gmail.com");

        subscription = new Subscription(user, SubscriptionType.FREE, SubscriptionStatus.ACTIVE, LocalDateTime.now(), null);
        subscription.setId(10L);
    }

    @Test
    void getActiveSubscription_existingActive_returnsSubscription() {
        when(subscriptionRepository.findByUserId(user.getId())).thenReturn(Optional.of(subscription));

        Subscription result = subscriptionService.getActiveSubscription(user);

        assertNotNull(result);
        assertEquals(SubscriptionType.FREE, result.getSubscriptionType());
        assertEquals(SubscriptionStatus.ACTIVE, result.getSubscriptionStatus());
        verify(subscriptionRepository, never()).save(any());
    }

    @Test
    void getActiveSubscription_notExisting_initializesFreeActive() {
        when(subscriptionRepository.findByUserId(user.getId())).thenReturn(Optional.empty());
        when(subscriptionRepository.save(any(Subscription.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Subscription result = subscriptionService.getActiveSubscription(user);

        assertNotNull(result);
        assertEquals(SubscriptionType.FREE, result.getSubscriptionType());
        assertEquals(SubscriptionStatus.ACTIVE, result.getSubscriptionStatus());
        verify(subscriptionRepository, times(1)).save(any(Subscription.class));
    }

    @Test
    void getActiveSubscription_expired_updatesStatusAndReturns() {
        subscription.setSubscriptionType(SubscriptionType.PREMIUM);
        subscription.setSubscriptionStatus(SubscriptionStatus.ACTIVE);
        subscription.setExpiryDate(LocalDateTime.now().minusDays(1)); // expired

        when(subscriptionRepository.findByUserId(user.getId())).thenReturn(Optional.of(subscription));
        when(subscriptionRepository.save(any(Subscription.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Subscription result = subscriptionService.getActiveSubscription(user);

        assertNotNull(result);
        assertEquals(SubscriptionStatus.EXPIRED, result.getSubscriptionStatus());
        verify(subscriptionRepository, times(1)).save(subscription);
    }

    @Test
    void upgradeSubscription_success() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(subscriptionRepository.findByUserId(user.getId())).thenReturn(Optional.of(subscription));
        when(subscriptionRepository.save(any(Subscription.class))).thenAnswer(invocation -> invocation.getArgument(0));

        SubscriptionResponseDto dto = subscriptionService.upgradeSubscription(user.getId(), SubscriptionType.PREMIUM, 30);

        assertNotNull(dto);
        assertEquals(SubscriptionType.PREMIUM, dto.subscriptionType());
        assertEquals(SubscriptionStatus.ACTIVE, dto.subscriptionStatus());
        assertNotNull(dto.expiryDate());
        verify(subscriptionRepository, times(1)).save(any(Subscription.class));
    }

    @Test
    void upgradeSubscription_userNotFound_throwsException() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () ->
                subscriptionService.upgradeSubscription(user.getId(), SubscriptionType.PREMIUM, 30));
    }

    @Test
    void downgradeSubscription_success() {
        subscription.setSubscriptionType(SubscriptionType.PREMIUM);
        subscription.setExpiryDate(LocalDateTime.now().plusDays(30));

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(subscriptionRepository.findByUserId(user.getId())).thenReturn(Optional.of(subscription));
        when(subscriptionRepository.save(any(Subscription.class))).thenAnswer(invocation -> invocation.getArgument(0));

        SubscriptionResponseDto dto = subscriptionService.downgradeSubscription(user.getId());

        assertNotNull(dto);
        assertEquals(SubscriptionType.FREE, dto.subscriptionType());
        assertNull(dto.expiryDate());
        verify(subscriptionRepository, times(1)).save(any(Subscription.class));
    }
}
