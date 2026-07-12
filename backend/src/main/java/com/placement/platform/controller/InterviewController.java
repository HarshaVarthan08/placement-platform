package com.placement.platform.controller;

import com.placement.platform.dto.*;
import com.placement.platform.service.InterviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/interview")
@Tag(name = "Interview", description = "Endpoints for managing interview sessions and question progression")
public class InterviewController {

    private final InterviewService interviewService;

    public InterviewController(InterviewService interviewService) {
        this.interviewService = interviewService;
    }

    @GetMapping("/availability")
    @Operation(summary = "Check interview availability", description = "Checks if the user has any cooldown constraint that prevents starting a new interview session.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved availability status")
    public ResponseEntity<InterviewAvailabilityResponseDto> checkAvailability() {
        InterviewAvailabilityResponseDto availability = interviewService.checkAvailability();
        return ResponseEntity.ok(availability);
    }

    @PostMapping("/start")
    @Operation(summary = "Start interview session", description = "Initiates a new practice interview session (Mock or Learning mode) based on the specified target company or custom settings.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Interview session successfully started"),
        @ApiResponse(responseCode = "400", description = "Invalid startup payload or user under active cooldown")
    })
    public ResponseEntity<InterviewSessionResponseDto> startInterview(
            @Valid @RequestBody StartInterviewRequestDto request) {
        InterviewSessionResponseDto session = interviewService.startInterview(request);
        return ResponseEntity.ok(session);
    }

    @GetMapping("/current-question")
    @Operation(summary = "Get current interview question", description = "Retrieves the active question details that the user needs to answer in the current active session.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully retrieved active question"),
        @ApiResponse(responseCode = "404", description = "No active interview session found")
    })
    public ResponseEntity<InterviewQuestionResponseDto> getCurrentQuestion() {
        InterviewQuestionResponseDto question = interviewService.getCurrentQuestion();
        return ResponseEntity.ok(question);
    }

    @PostMapping("/answer")
    @Operation(summary = "Submit answer for current question", description = "Submits the user's audio/text transcription response for the active question.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Answer successfully recorded"),
        @ApiResponse(responseCode = "400", description = "Invalid submission details or no active question")
    })
    public ResponseEntity<Void> answerCurrentQuestion(@Valid @RequestBody SubmitAnswerRequestDto request) {
        interviewService.answerCurrentQuestion(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/next")
    @Operation(summary = "Advance to next question", description = "Saves current progress and increments index to return the next question details.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully advanced to next question"),
        @ApiResponse(responseCode = "400", description = "Cannot advance (either no active session or already at final question)")
    })
    public ResponseEntity<InterviewProgressResponseDto> goToNextQuestion() {
        InterviewProgressResponseDto progress = interviewService.goToNextQuestion();
        return ResponseEntity.ok(progress);
    }

    @GetMapping("/progress")
    @Operation(summary = "Get session progress status", description = "Retrieves the progress details (completed questions count, remaining count) of the active session.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved session progress")
    public ResponseEntity<InterviewProgressResponseDto> getProgress() {
        InterviewProgressResponseDto progress = interviewService.getProgress();
        return ResponseEntity.ok(progress);
    }

    @PostMapping("/complete")
    @Operation(summary = "Complete interview session", description = "Finalizes the active session and locks all answers for AI evaluation processing.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Session finalized successfully"),
        @ApiResponse(responseCode = "400", description = "No active session to complete")
    })
    public ResponseEntity<InterviewSessionResponseDto> completeInterview() {
        InterviewSessionResponseDto session = interviewService.completeInterview();
        return ResponseEntity.ok(session);
    }
}
