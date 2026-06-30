package com.placement.platform.service.dashboard;

import com.placement.platform.dto.dashboard.PlacementReadinessCard;
import com.placement.platform.entity.User;
import com.placement.platform.service.dashboard.PlacementDataAggregator.PlacementData;

public interface ReadinessService {
    PlacementReadinessCard calculateReadiness(User user, PlacementData data);
}
