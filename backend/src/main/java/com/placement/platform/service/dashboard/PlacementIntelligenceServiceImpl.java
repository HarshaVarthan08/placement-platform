package com.placement.platform.service.dashboard;

import com.placement.platform.dto.dashboard.DashboardResponseDto;
import com.placement.platform.entity.User;
import org.springframework.stereotype.Service;

@Service
public class PlacementIntelligenceServiceImpl implements PlacementIntelligenceService {

    private final PlacementDataAggregator dataAggregator;
    private final DashboardAnalyticsService analyticsService;

    public PlacementIntelligenceServiceImpl(
            PlacementDataAggregator dataAggregator,
            DashboardAnalyticsService analyticsService
    ) {
        this.dataAggregator = dataAggregator;
        this.analyticsService = analyticsService;
    }

    @Override
    public DashboardResponseDto getDashboardResponse(User user) {
        PlacementDataAggregator.PlacementData data = dataAggregator.aggregate(user);
        return analyticsService.buildDashboardResponse(user, data);
    }
}
