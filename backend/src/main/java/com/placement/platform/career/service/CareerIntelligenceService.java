package com.placement.platform.career.service;

import com.placement.platform.career.intelligence.CareerIntelligenceProfile;
import com.placement.platform.entity.User;

public interface CareerIntelligenceService {
    CareerIntelligenceProfile getProfile(Long recommendationId, User authenticatedUser);
}
