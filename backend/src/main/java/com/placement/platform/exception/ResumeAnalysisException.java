package com.placement.platform.exception;

public class ResumeAnalysisException extends RuntimeException {
    public ResumeAnalysisException(String message) {
        super(message);
    }

    public ResumeAnalysisException(String message, Throwable cause) {
        super(message, cause);
    }
}
