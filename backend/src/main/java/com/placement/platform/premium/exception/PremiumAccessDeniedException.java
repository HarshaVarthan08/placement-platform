package com.placement.platform.premium.exception;

public class PremiumAccessDeniedException extends RuntimeException {
    public PremiumAccessDeniedException(String message) {
        super(message);
    }
}
