package com.placement.platform.controller;

import com.placement.platform.dto.InterviewEvaluationResponseDto;
import com.placement.platform.dto.InterviewEvaluationStatusResponseDto;
import com.placement.platform.service.InterviewEvaluationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/interview")
public class InterviewEvaluationController {

    private final InterviewEvaluationService interviewEvaluationService;

    public InterviewEvaluationController(InterviewEvaluationService interviewEvaluationService) {
        this.interviewEvaluationService = interviewEvaluationService;
    }

    @PostMapping("/evaluate")
    public ResponseEntity<InterviewEvaluationResponseDto> evaluateInterview(
            @RequestParam(value = "sessionId", required = false) Long sessionId) {
        InterviewEvaluationResponseDto response = interviewEvaluationService.evaluateInterview(sessionId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/evaluation")
    public ResponseEntity<InterviewEvaluationResponseDto> getEvaluation(
            @RequestParam(value = "sessionId", required = false) Long sessionId) {
        InterviewEvaluationResponseDto response = interviewEvaluationService.getEvaluation(sessionId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/evaluation/status")
    public ResponseEntity<InterviewEvaluationStatusResponseDto> getEvaluationStatus(
            @RequestParam(value = "sessionId", required = false) Long sessionId) {
        InterviewEvaluationStatusResponseDto response = interviewEvaluationService.getEvaluationStatus(sessionId);
        return ResponseEntity.ok(response);
    }
}
