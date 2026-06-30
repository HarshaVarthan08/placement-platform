package com.placement.platform.service.dashboard;

import com.placement.platform.dto.dashboard.DashboardResponseDto;
import com.placement.platform.entity.User;

public interface PlacementIntelligenceService {
    DashboardResponseDto getDashboardResponse(User user);
}
