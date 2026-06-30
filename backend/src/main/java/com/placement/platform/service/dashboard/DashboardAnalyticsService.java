package com.placement.platform.service.dashboard;

import com.placement.platform.dto.dashboard.DashboardResponseDto;
import com.placement.platform.entity.User;
import com.placement.platform.service.dashboard.PlacementDataAggregator.PlacementData;

public interface DashboardAnalyticsService {
    DashboardResponseDto buildDashboardResponse(User user, PlacementData data);
}
