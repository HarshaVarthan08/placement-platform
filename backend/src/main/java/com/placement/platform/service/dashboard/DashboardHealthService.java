package com.placement.platform.service.dashboard;

import com.placement.platform.dto.dashboard.DashboardHealthCard;
import com.placement.platform.entity.User;
import com.placement.platform.service.dashboard.PlacementDataAggregator.PlacementData;

public interface DashboardHealthService {
    DashboardHealthCard calculateHealth(User user, PlacementData data);
}
