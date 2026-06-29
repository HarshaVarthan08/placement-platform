package com.placement.platform.controller;

import com.placement.platform.dto.*;
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

    @GetMapping("/current-question")
    public ResponseEntity<InterviewQuestionResponseDto> getCurrentQuestion() {
        InterviewQuestionResponseDto question = interviewService.getCurrentQuestion();
        return ResponseEntity.ok(question);
    }

    @PostMapping("/answer")
    public ResponseEntity<Void> answerCurrentQuestion(@Valid @RequestBody SubmitAnswerRequestDto request) {
        interviewService.answerCurrentQuestion(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/next")
    public ResponseEntity<InterviewProgressResponseDto> goToNextQuestion() {
        InterviewProgressResponseDto progress = interviewService.goToNextQuestion();
        return ResponseEntity.ok(progress);
    }

    @GetMapping("/progress")
    public ResponseEntity<InterviewProgressResponseDto> getProgress() {
        InterviewProgressResponseDto progress = interviewService.getProgress();
        return ResponseEntity.ok(progress);
    }

    @PostMapping("/complete")
    public ResponseEntity<InterviewSessionResponseDto> completeInterview() {
        InterviewSessionResponseDto session = interviewService.completeInterview();
        return ResponseEntity.ok(session);
    }
}
