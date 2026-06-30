package com.placement.platform.service;

import com.placement.platform.dto.ResumeAnalysisResponseDto;
import org.springframework.stereotype.Service;

@Service
public class AIServiceImpl implements AIService {

    private final GeminiAIService geminiAIService;

    public AIServiceImpl(GeminiAIService geminiAIService) {
        this.geminiAIService = geminiAIService;
    }

    @Override
    public ResumeAnalysisResponseDto analyzeResumeText(String resumeText) {
        return geminiAIService.analyzeResumeText(resumeText);
    }

    @Override
    public String evaluateInterview(String evaluationPrompt) {
        return geminiAIService.evaluateInterview(evaluationPrompt);
    }

    @Override
    public String getModelUsed() {
        return geminiAIService.getModelUsed();
    }
}
