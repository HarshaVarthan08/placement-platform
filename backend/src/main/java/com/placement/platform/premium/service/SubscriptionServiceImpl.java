package com.placement.platform.premium.service;

import com.placement.platform.entity.User;
import com.placement.platform.exception.UserNotFoundException;
import com.placement.platform.premium.dto.SubscriptionResponseDto;
import com.placement.platform.premium.entity.Subscription;
import com.placement.platform.premium.enums.SubscriptionStatus;
import com.placement.platform.premium.enums.SubscriptionType;
import com.placement.platform.premium.repository.SubscriptionRepository;
import com.placement.platform.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class SubscriptionServiceImpl implements SubscriptionService {

    private static final Logger log = LoggerFactory.getLogger(SubscriptionServiceImpl.class);

    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;

    public SubscriptionServiceImpl(SubscriptionRepository subscriptionRepository, UserRepository userRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Subscription getActiveSubscription(User user) {
        Subscription subscription = subscriptionRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    Subscription newSub = new Subscription(
                            user,
                            SubscriptionType.FREE,
                            SubscriptionStatus.ACTIVE,
                            LocalDateTime.now(),
                            null
                    );
                    Subscription saved = subscriptionRepository.save(newSub);
                    log.info("Subscription initialized. User:{}. Type:FREE. Status:ACTIVE", user.getId());
                    return saved;
                });

        // Dynamic Expiry Check
        if ((subscription.getSubscriptionStatus() == SubscriptionStatus.ACTIVE ||
             subscription.getSubscriptionStatus() == SubscriptionStatus.TRIAL) &&
            subscription.getExpiryDate() != null &&
            subscription.getExpiryDate().isBefore(LocalDateTime.now())) {
            
            subscription.setSubscriptionStatus(SubscriptionStatus.EXPIRED);
            subscription = subscriptionRepository.save(subscription);
            log.info("Subscription status updated to EXPIRED on lookup. User:{}", user.getId());
        }

        return subscription;
    }

    @Override
    public SubscriptionResponseDto getActiveSubscriptionDto(User user) {
        Subscription sub = getActiveSubscription(user);
        return mapToDto(sub);
    }

    @Override
    public SubscriptionResponseDto upgradeSubscription(Long userId, SubscriptionType type, Integer durationDays) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        Subscription sub = subscriptionRepository.findByUserId(userId)
                .orElseGet(() -> new Subscription(user, SubscriptionType.FREE, SubscriptionStatus.ACTIVE, LocalDateTime.now(), null));

        sub.setSubscriptionType(type);
        sub.setSubscriptionStatus(SubscriptionStatus.ACTIVE);
        sub.setStartDate(LocalDateTime.now());
        
        if (durationDays != null && durationDays > 0) {
            sub.setExpiryDate(LocalDateTime.now().plusDays(durationDays));
        } else {
            sub.setExpiryDate(null); // permanent or default unlimited
        }

        Subscription saved = subscriptionRepository.save(sub);
        log.info("Subscription upgraded. User:{}. Type:{}. Duration:{} days", userId, type, durationDays != null ? durationDays : "unlimited");
        return mapToDto(saved);
    }

    @Override
    public SubscriptionResponseDto downgradeSubscription(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        Subscription sub = subscriptionRepository.findByUserId(userId)
                .orElseGet(() -> new Subscription(user, SubscriptionType.FREE, SubscriptionStatus.ACTIVE, LocalDateTime.now(), null));

        sub.setSubscriptionType(SubscriptionType.FREE);
        sub.setSubscriptionStatus(SubscriptionStatus.ACTIVE);
        sub.setStartDate(LocalDateTime.now());
        sub.setExpiryDate(null);

        Subscription saved = subscriptionRepository.save(sub);
        log.info("Subscription downgraded. User:{}. Type:FREE", userId);
        return mapToDto(saved);
    }

    @Override
    public void initializeFreeSubscription(User user) {
        if (subscriptionRepository.findByUserId(user.getId()).isEmpty()) {
            Subscription newSub = new Subscription(
                    user,
                    SubscriptionType.FREE,
                    SubscriptionStatus.ACTIVE,
                    LocalDateTime.now(),
                    null
            );
            subscriptionRepository.save(newSub);
            log.info("Subscription initialized. User:{}. Type:FREE. Status:ACTIVE", user.getId());
        }
    }

    @Override
    public SubscriptionResponseDto getSubscriptionDtoByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
        return getActiveSubscriptionDto(user);
    }

    private SubscriptionResponseDto mapToDto(Subscription sub) {
        return new SubscriptionResponseDto(
                sub.getId(),
                sub.getUser().getId(),
                sub.getUser().getEmail(),
                sub.getSubscriptionType(),
                sub.getSubscriptionStatus(),
                sub.getStartDate(),
                sub.getExpiryDate(),
                sub.getCreatedAt(),
                sub.getUpdatedAt()
        );
    }
}
