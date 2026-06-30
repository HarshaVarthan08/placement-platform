package com.placement.platform.service;

import com.placement.platform.dto.InterviewEvaluationResponseDto;
import com.placement.platform.dto.InterviewEvaluationStatusResponseDto;

public interface InterviewEvaluationService {
    InterviewEvaluationResponseDto evaluateInterview(Long sessionId);
    InterviewEvaluationResponseDto evaluateInterviewTransactional(Long sessionId);
    InterviewEvaluationResponseDto getEvaluation(Long sessionId);
    InterviewEvaluationStatusResponseDto getEvaluationStatus(Long sessionId);
}
