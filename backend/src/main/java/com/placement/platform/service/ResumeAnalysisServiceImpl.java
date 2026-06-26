package com.placement.platform.service;

import com.placement.platform.dto.ResumeAnalysisResponseDto;
import com.placement.platform.entity.Resume;
import com.placement.platform.entity.ResumeAnalysis;
import com.placement.platform.entity.User;
import com.placement.platform.exception.ResourceNotFoundException;
import com.placement.platform.exception.UserNotFoundException;
import com.placement.platform.mapper.ResumeAnalysisMapper;
import com.placement.platform.repository.ResumeAnalysisRepository;
import com.placement.platform.repository.ResumeRepository;
import com.placement.platform.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ResumeAnalysisServiceImpl implements ResumeAnalysisService {

    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;
    private final ResumeAnalysisRepository resumeAnalysisRepository;
    private final ResumeTextExtractor resumeTextExtractor;
    private final AIService aiService;
    private final ResumeAnalysisMapper resumeAnalysisMapper;

    public ResumeAnalysisServiceImpl(
            ResumeRepository resumeRepository,
            UserRepository userRepository,
            ResumeAnalysisRepository resumeAnalysisRepository,
            ResumeTextExtractor resumeTextExtractor,
            AIService aiService,
            ResumeAnalysisMapper resumeAnalysisMapper
    ) {
        this.resumeRepository = resumeRepository;
        this.userRepository = userRepository;
        this.resumeAnalysisRepository = resumeAnalysisRepository;
        this.resumeTextExtractor = resumeTextExtractor;
        this.aiService = aiService;
        this.resumeAnalysisMapper = resumeAnalysisMapper;
    }

    @Override
    @Transactional
    public ResumeAnalysisResponseDto analyzeResume() {
        User user = getAuthenticatedUser();
        Resume resume = resumeRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("No resume found for the current user. Please upload a resume first."));

        // Check cache
        if (resumeAnalysisRepository.existsByResume(resume)) {
            ResumeAnalysis cachedAnalysis = resumeAnalysisRepository.findByResume(resume)
                    .orElseThrow(() -> new ResourceNotFoundException("Cached resume analysis not found."));
            return resumeAnalysisMapper.toDto(cachedAnalysis);
        }

        // Text extraction
        String extractedText = resumeTextExtractor.extractText(resume.getFilePath());

        // Call AI Service
        ResumeAnalysisResponseDto aiResponse = aiService.analyzeResumeText(extractedText);

        // Build and Save Entity
        ResumeAnalysis analysis = new ResumeAnalysis();
        analysis.setResume(resume);
        analysis.setAtsScore(aiResponse.atsScore());
        analysis.setSummary(aiResponse.summary());
        analysis.setStrengths(aiResponse.strengths());
        analysis.setWeaknesses(aiResponse.weaknesses());
        analysis.setMissingSkills(aiResponse.missingSkills());
        analysis.setSuggestions(aiResponse.suggestions());
        analysis.setModelUsed(aiService.getModelUsed());

        ResumeAnalysis savedAnalysis = resumeAnalysisRepository.save(analysis);

        return resumeAnalysisMapper.toDto(savedAnalysis);
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UserNotFoundException("User is not authenticated");
        }
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
    }
}
