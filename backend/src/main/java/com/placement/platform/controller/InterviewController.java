package com.placement.platform.controller;

import com.placement.platform.dto.InterviewAvailabilityResponseDto;
import com.placement.platform.dto.InterviewSessionResponseDto;
import com.placement.platform.dto.StartInterviewRequestDto;
import com.placement.platform.service.InterviewService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/interview")
public class InterviewController {

    private final InterviewService interviewService;

    public InterviewController(InterviewService interviewService) {
        this.interviewService = interviewService;
    }

    @GetMapping("/availability")
    public ResponseEntity<InterviewAvailabilityResponseDto> checkAvailability() {
        InterviewAvailabilityResponseDto availability = interviewService.checkAvailability();
        return ResponseEntity.ok(availability);
    }

    @PostMapping("/start")
    public ResponseEntity<InterviewSessionResponseDto> startInterview(
            @Valid @RequestBody StartInterviewRequestDto request) {
        InterviewSessionResponseDto session = interviewService.startInterview(request);
        return ResponseEntity.ok(session);
    }
}
