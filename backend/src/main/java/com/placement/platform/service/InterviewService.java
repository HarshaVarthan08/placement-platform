package com.placement.platform.service;

import com.placement.platform.dto.*;

public interface InterviewService {
    InterviewAvailabilityResponseDto checkAvailability();
    InterviewSessionResponseDto startInterview(StartInterviewRequestDto request);
    InterviewQuestionResponseDto getCurrentQuestion();
    void answerCurrentQuestion(SubmitAnswerRequestDto request);
    InterviewProgressResponseDto getProgress();
    InterviewProgressResponseDto goToNextQuestion();
    InterviewSessionResponseDto completeInterview();
}
