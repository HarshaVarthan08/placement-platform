package com.placement.platform.service.dashboard;

import com.placement.platform.dto.dashboard.RecommendationCard;
import com.placement.platform.entity.User;
import com.placement.platform.service.dashboard.PlacementDataAggregator.PlacementData;

public interface RecommendationEngine {
    RecommendationCard generateRecommendations(User user, PlacementData data);
}
