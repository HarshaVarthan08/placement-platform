package com.placement.platform.premium.service;

import com.placement.platform.entity.User;
import com.placement.platform.premium.dto.FeatureAvailability;
import com.placement.platform.premium.dto.PremiumAccessResult;
import com.placement.platform.premium.entity.Subscription;
import com.placement.platform.premium.enums.FeatureStatus;
import com.placement.platform.premium.enums.PremiumFeature;
import com.placement.platform.premium.enums.SubscriptionStatus;
import com.placement.platform.premium.enums.SubscriptionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PremiumAccessServiceImpl implements PremiumAccessService {

    private static final Logger log = LoggerFactory.getLogger(PremiumAccessServiceImpl.class);

    private final SubscriptionService subscriptionService;
    private final FeatureAvailabilityService availabilityService;

    public PremiumAccessServiceImpl(SubscriptionService subscriptionService, FeatureAvailabilityService availabilityService) {
        this.subscriptionService = subscriptionService;
        this.availabilityService = availabilityService;
    }

    @Override
    public PremiumAccessResult checkAccess(User user, PremiumFeature feature) {
        Subscription sub = subscriptionService.getActiveSubscription(user);
        FeatureAvailability availability = availabilityService.getAvailability(feature);

        boolean allowed = false;
        String reason;

        if (availability.status() == FeatureStatus.DISABLED) {
            reason = "FEATURE_DISABLED";
        } else if (availability.status() == FeatureStatus.COMING_SOON) {
            reason = "FEATURE_COMING_SOON";
        } else {
            // Feature is AVAILABLE or BETA
            if (sub.getSubscriptionType() == SubscriptionType.FREE) {
                reason = "PREMIUM_REQUIRED";
            } else if (sub.getSubscriptionStatus() != SubscriptionStatus.ACTIVE &&
                       sub.getSubscriptionStatus() != SubscriptionStatus.TRIAL) {
                reason = "SUBSCRIPTION_INACTIVE";
            } else {
                allowed = true;
                reason = "ACCESS_GRANTED";
            }
        }

        PremiumAccessResult result = new PremiumAccessResult(
                allowed,
                reason,
                sub.getSubscriptionType(),
                availability.status()
        );

        log.info("Premium Access Check. User:{}. Feature:{}. Access:{}. Type:{}. Status:{}. Reason:{}",
                user.getId(), feature, result.allowed(), sub.getSubscriptionType(), sub.getSubscriptionStatus(), result.reason());

        return result;
    }

    @Override
    public boolean isPremium(User user) {
        Subscription sub = subscriptionService.getActiveSubscription(user);
        return sub.getSubscriptionType() == SubscriptionType.PREMIUM &&
                (sub.getSubscriptionStatus() == SubscriptionStatus.ACTIVE ||
                 sub.getSubscriptionStatus() == SubscriptionStatus.TRIAL);
    }

    @Override
    public Subscription getActiveSubscription(User user) {
        return subscriptionService.getActiveSubscription(user);
    }
}
