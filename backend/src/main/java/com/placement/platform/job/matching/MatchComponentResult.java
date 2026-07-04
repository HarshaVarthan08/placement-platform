package com.placement.platform.job.matching;

import java.util.List;

/**
 * Capture the result of evaluating a single match strategy.
 */
public record MatchComponentResult(
    String componentName,
    Integer score,
    Integer maximumScore,
    String reason,
    List<String> matchedItems,
    List<String> missingItems
) {}
