package com.placement.platform.job.recommendation;

import com.placement.platform.entity.User;
import com.placement.platform.job.dto.RecommendationSummaryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RecommendationService {

    List<JobRecommendation> generateRecommendations(User user);

    List<JobRecommendation> generateRecommendations(User user, RecommendationGenerationReason reason);

    Page<JobRecommendation> getRecommendations(User user, Pageable pageable);

    JobRecommendation getRecommendationDetails(Long id);

    RecommendationSummaryDto getRecommendationSummary(User user);
}
