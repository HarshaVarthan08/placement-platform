package com.placement.platform.exception;

public class QuestionPoolGenerationException extends RuntimeException {
    public QuestionPoolGenerationException(String message) {
        super(message);
    }

    public QuestionPoolGenerationException(String message, Throwable cause) {
        super(message, cause);
    }
}
