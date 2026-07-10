package com.placement.platform.job.recommendation;

public interface RecommendationLifecycleService {
    JobRecommendation markViewed(Long id);
    JobRecommendation saveRecommendation(Long id);
    JobRecommendation unsaveRecommendation(Long id);
    JobRecommendation hideRecommendation(Long id);
    JobRecommendation archiveRecommendation(Long id);
    JobRecommendation expireRecommendation(Long id);
    JobRecommendation submitFeedback(Long id, RecommendationFeedbackType feedbackType, String feedbackNotes);
}
