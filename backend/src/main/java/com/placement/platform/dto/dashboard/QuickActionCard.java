package com.placement.platform.dto.dashboard;

import java.time.LocalDateTime;
import java.util.List;

public record QuickActionCard(
    String status,
    List<QuickActionDto> actions,
    LocalDateTime lastUpdated
) {}
