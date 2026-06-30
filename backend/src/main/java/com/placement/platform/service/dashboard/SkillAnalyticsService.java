package com.placement.platform.service.dashboard;

import com.placement.platform.dto.dashboard.SkillAnalyticsCard;
import com.placement.platform.entity.User;
import com.placement.platform.service.dashboard.PlacementDataAggregator.PlacementData;

public interface SkillAnalyticsService {
    SkillAnalyticsCard analyzeSkills(User user, PlacementData data);
}
