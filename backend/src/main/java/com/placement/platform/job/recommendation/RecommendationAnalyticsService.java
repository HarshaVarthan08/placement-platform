package com.placement.platform.job.recommendation;

import com.placement.platform.entity.User;
import com.placement.platform.job.dto.RecommendationAnalyticsResponseDto;

public interface RecommendationAnalyticsService {
    RecommendationAnalyticsResponseDto getAnalytics(User user, boolean includeHistory);
}
