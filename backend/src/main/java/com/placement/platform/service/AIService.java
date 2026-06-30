package com.placement.platform.service;

import com.placement.platform.dto.ResumeAnalysisResponseDto;

public interface AIService {
    ResumeAnalysisResponseDto analyzeResumeText(String resumeText);
    String evaluateInterview(String evaluationPrompt);
    String getModelUsed();
}
