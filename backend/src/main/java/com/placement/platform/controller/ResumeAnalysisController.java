package com.placement.platform.controller;

import com.placement.platform.dto.ResumeAnalysisResponseDto;
import com.placement.platform.service.ResumeAnalysisService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/resume")
public class ResumeAnalysisController {

    private final ResumeAnalysisService resumeAnalysisService;

    public ResumeAnalysisController(ResumeAnalysisService resumeAnalysisService) {
        this.resumeAnalysisService = resumeAnalysisService;
    }

    @PostMapping("/analyze")
    public ResponseEntity<ResumeAnalysisResponseDto> analyzeResume() {
        ResumeAnalysisResponseDto response = resumeAnalysisService.analyzeResume();
        return ResponseEntity.ok(response);
    }
}
