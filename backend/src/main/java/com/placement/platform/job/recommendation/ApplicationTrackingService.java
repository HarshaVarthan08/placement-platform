package com.placement.platform.job.recommendation;

import com.placement.platform.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ApplicationTrackingService {
    JobRecommendation applyForRecommendation(Long id, String url, String reference, String notes);
    JobRecommendation updateApplicationStage(Long id, ApplicationStatus status, String notes);
    Page<JobRecommendation> getAppliedRecommendations(User user, Pageable pageable);
}
