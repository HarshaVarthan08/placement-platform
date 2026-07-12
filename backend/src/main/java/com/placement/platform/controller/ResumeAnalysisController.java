package com.placement.platform.controller;

import com.placement.platform.dto.ResumeAnalysisResponseDto;
import com.placement.platform.service.ResumeAnalysisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/resume")
@Tag(name = "Resume", description = "Endpoints for resume uploading and AI analysis processing")
public class ResumeAnalysisController {

    private final ResumeAnalysisService resumeAnalysisService;

    public ResumeAnalysisController(ResumeAnalysisService resumeAnalysisService) {
        this.resumeAnalysisService = resumeAnalysisService;
    }

    @PostMapping("/analyze")
    @Operation(summary = "Analyze active resume", description = "Parses the active uploaded resume and evaluates it using AI to extract skills, calculate ATS score, profile match rate, and suggest optimizations.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully analyzed resume"),
        @ApiResponse(responseCode = "400", description = "No resume uploaded or analysis failed")
    })
    public ResponseEntity<ResumeAnalysisResponseDto> analyzeResume() {
        ResumeAnalysisResponseDto response = resumeAnalysisService.analyzeResume();
        return ResponseEntity.ok(response);
    }
}
