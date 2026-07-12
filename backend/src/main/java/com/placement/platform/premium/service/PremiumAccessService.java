package com.placement.platform.premium.service;

import com.placement.platform.entity.User;
import com.placement.platform.premium.dto.PremiumAccessResult;
import com.placement.platform.premium.entity.Subscription;
import com.placement.platform.premium.enums.PremiumFeature;

public interface PremiumAccessService {
    PremiumAccessResult checkAccess(User user, PremiumFeature feature);
    boolean isPremium(User user);
    Subscription getActiveSubscription(User user);
}
