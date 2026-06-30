package com.placement.platform.exception;

public class InterviewEvaluationException extends RuntimeException {
    public InterviewEvaluationException(String message) {
        super(message);
    }

    public InterviewEvaluationException(String message, Throwable cause) {
        super(message, cause);
    }
}
