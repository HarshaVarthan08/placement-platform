package com.placement.platform.job.dto;

import com.placement.platform.job.recommendation.RecommendationFeedbackType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class FeedbackRequestDto {
    @NotNull
    private RecommendationFeedbackType feedbackType;

    @Size(max = 2000)
    private String feedbackNotes;

    public FeedbackRequestDto() {}

    public RecommendationFeedbackType getFeedbackType() {
        return feedbackType;
    }

    public void setFeedbackType(RecommendationFeedbackType feedbackType) {
        this.feedbackType = feedbackType;
    }

    public String getFeedbackNotes() {
        return feedbackNotes;
    }

    public void setFeedbackNotes(String feedbackNotes) {
        this.feedbackNotes = feedbackNotes;
    }
}
