package com.placement.platform.exception;

import java.time.LocalDateTime;

public record ErrorResponse(
    int status,
    String message,
    LocalDateTime timestamp,
    Object details
) {
    public ErrorResponse(int status, String message) {
        this(status, message, LocalDateTime.now(), null);
    }

    public ErrorResponse(int status, String message, Object details) {
        this(status, message, LocalDateTime.now(), details);
    }
}
