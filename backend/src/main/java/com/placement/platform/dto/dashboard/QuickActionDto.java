package com.placement.platform.dto.dashboard;

public record QuickActionDto(
    String title,
    String actionUrl,
    String type,
    String priority,
    Boolean completed
) {}
