package com.placement.platform.controller;

import com.placement.platform.dto.InterviewEvaluationResponseDto;
import com.placement.platform.dto.InterviewEvaluationStatusResponseDto;
import com.placement.platform.service.InterviewEvaluationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/interview")
@Tag(name = "Interview", description = "Endpoints for managing interview sessions and question progression")
public class InterviewEvaluationController {

    private final InterviewEvaluationService interviewEvaluationService;

    public InterviewEvaluationController(InterviewEvaluationService interviewEvaluationService) {
        this.interviewEvaluationService = interviewEvaluationService;
    }

    @PostMapping("/evaluate")
    @Operation(summary = "Submit interview for evaluation", description = "Submits the interview responses to the AI intelligence evaluator to compute ATS score, clarity, relevant feedback, and readiness metrics.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Evaluation successfully started or completed"),
        @ApiResponse(responseCode = "400", description = "No session found or session is not finalized yet")
    })
    public ResponseEntity<InterviewEvaluationResponseDto> evaluateInterview(
            @RequestParam(value = "sessionId", required = false) Long sessionId) {
        InterviewEvaluationResponseDto response = interviewEvaluationService.evaluateInterview(sessionId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/evaluation")
    @Operation(summary = "Get interview evaluation details", description = "Retrieves the detailed AI feedback report and evaluation metrics for a specific completed session.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully retrieved evaluation details"),
        @ApiResponse(responseCode = "404", description = "Evaluation report not found")
    })
    public ResponseEntity<InterviewEvaluationResponseDto> getEvaluation(
            @RequestParam(value = "sessionId", required = false) Long sessionId) {
        InterviewEvaluationResponseDto response = interviewEvaluationService.getEvaluation(sessionId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/evaluation/status")
    @Operation(summary = "Get interview evaluation status", description = "Checks the active state (COMPLETED, PROCESSING, NOT_STARTED, etc.) of the evaluation execution.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved evaluation status")
    public ResponseEntity<InterviewEvaluationStatusResponseDto> getEvaluationStatus(
            @RequestParam(value = "sessionId", required = false) Long sessionId) {
        InterviewEvaluationStatusResponseDto response = interviewEvaluationService.getEvaluationStatus(sessionId);
        return ResponseEntity.ok(response);
    }
}
