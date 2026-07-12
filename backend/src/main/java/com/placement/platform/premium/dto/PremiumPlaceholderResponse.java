package com.placement.platform.premium.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record PremiumPlaceholderResponse(
    boolean success,
    String status,
    Boolean premiumRequired,
    String message
) {}
