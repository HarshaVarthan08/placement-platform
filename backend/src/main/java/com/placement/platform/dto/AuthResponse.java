package com.placement.platform.dto;

public record AuthResponse(
    String token,
    Long userId
) {}
