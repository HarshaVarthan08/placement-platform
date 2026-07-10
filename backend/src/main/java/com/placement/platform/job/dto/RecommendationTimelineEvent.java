package com.placement.platform.job.dto;

import java.time.LocalDateTime;

public class RecommendationTimelineEvent {
    private String eventType;
    private LocalDateTime timestamp;
    private String title;
    private String description;

    public RecommendationTimelineEvent() {}

    public RecommendationTimelineEvent(String eventType, LocalDateTime timestamp, String title, String description) {
        this.eventType = eventType;
        this.timestamp = timestamp;
        this.title = title;
        this.description = description;
    }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
