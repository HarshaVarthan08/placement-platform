package com.placement.platform.service;

import com.placement.platform.dto.InterviewAvailabilityResponseDto;
import com.placement.platform.dto.InterviewSessionResponseDto;
import com.placement.platform.dto.StartInterviewRequestDto;

public interface InterviewService {
    InterviewAvailabilityResponseDto checkAvailability();
    InterviewSessionResponseDto startInterview(StartInterviewRequestDto request);
}
