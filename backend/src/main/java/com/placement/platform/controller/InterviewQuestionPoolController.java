package com.placement.platform.controller;

import com.placement.platform.dto.QuestionPoolResponseDto;
import com.placement.platform.dto.QuestionPoolStatusDto;
import com.placement.platform.entity.*;
import com.placement.platform.exception.UserNotFoundException;
import com.placement.platform.mapper.InterviewQuestionPoolMapper;
import com.placement.platform.repository.InterviewQuestionPoolRepository;
import com.placement.platform.repository.InterviewQuestionRepository;
import com.placement.platform.repository.UserRepository;
import com.placement.platform.service.InterviewProfileService;
import com.placement.platform.service.QuestionPoolGenerationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/interview/question-pool")
public class InterviewQuestionPoolController {

    private final UserRepository userRepository;
    private final InterviewProfileService interviewProfileService;
    private final QuestionPoolGenerationService questionPoolGenerationService;
    private final InterviewQuestionPoolRepository interviewQuestionPoolRepository;
    private final InterviewQuestionRepository interviewQuestionRepository;
    private final InterviewQuestionPoolMapper interviewQuestionPoolMapper;

    public InterviewQuestionPoolController(
            UserRepository userRepository,
            InterviewProfileService interviewProfileService,
            QuestionPoolGenerationService questionPoolGenerationService,
            InterviewQuestionPoolRepository interviewQuestionPoolRepository,
            InterviewQuestionRepository interviewQuestionRepository,
            InterviewQuestionPoolMapper interviewQuestionPoolMapper
    ) {
        this.userRepository = userRepository;
        this.interviewProfileService = interviewProfileService;
        this.questionPoolGenerationService = questionPoolGenerationService;
        this.interviewQuestionPoolRepository = interviewQuestionPoolRepository;
        this.interviewQuestionRepository = interviewQuestionRepository;
        this.interviewQuestionPoolMapper = interviewQuestionPoolMapper;
    }

    @PostMapping("/generate")
    public ResponseEntity<QuestionPoolResponseDto> generateQuestionPool(
            @RequestParam(value = "difficulty", required = false, defaultValue = "MEDIUM") InterviewDifficulty difficulty
    ) {
        User user = getAuthenticatedUser();
        InterviewProfile profile = interviewProfileService.getOrCreateProfile(user);
        int currentProfileVersion = profile.getProfileVersion();

        // Check if an ACTIVE pool matching the current version already exists
        Optional<InterviewQuestionPool> activePoolOpt = interviewQuestionPoolRepository
                .findByUserAndStatus(user, QuestionPoolStatus.ACTIVE);

        if (activePoolOpt.isPresent()) {
            InterviewQuestionPool activePool = activePoolOpt.get();
            if (activePool.getProfileVersion() == currentProfileVersion) {
                List<InterviewQuestion> questions = interviewQuestionRepository.findByQuestionPool(activePool);
                return ResponseEntity.ok(interviewQuestionPoolMapper.toDto(activePool, questions));
            }
        }

        // Generate new pool (will archive outdated/active pools internally)
        InterviewQuestionPool newPool = questionPoolGenerationService.generateQuestionPool(user, difficulty);
        List<InterviewQuestion> questions = interviewQuestionRepository.findByQuestionPool(newPool);

        return ResponseEntity.ok(interviewQuestionPoolMapper.toDto(newPool, questions));
    }

    @GetMapping("/status")
    public ResponseEntity<QuestionPoolStatusDto> getQuestionPoolStatus() {
        User user = getAuthenticatedUser();
        InterviewProfile profile = interviewProfileService.getOrCreateProfile(user);
        int currentProfileVersion = profile.getProfileVersion();

        Optional<InterviewQuestionPool> activePoolOpt = interviewQuestionPoolRepository
                .findByUserAndStatus(user, QuestionPoolStatus.ACTIVE);
        if (activePoolOpt.isPresent()) {
            InterviewQuestionPool pool = activePoolOpt.get();
            return ResponseEntity.ok(new QuestionPoolStatusDto(
                    pool.getStatus().name(),
                    currentProfileVersion,
                    pool.getProfileVersion(),
                    pool.getGeneratedAt()
            ));
        }

        Optional<InterviewQuestionPool> outdatedPoolOpt = interviewQuestionPoolRepository
                .findByUserAndStatus(user, QuestionPoolStatus.OUTDATED);
        if (outdatedPoolOpt.isPresent()) {
            InterviewQuestionPool pool = outdatedPoolOpt.get();
            return ResponseEntity.ok(new QuestionPoolStatusDto(
                    pool.getStatus().name(),
                    currentProfileVersion,
                    pool.getProfileVersion(),
                    pool.getGeneratedAt()
            ));
        }

        return ResponseEntity.ok(new QuestionPoolStatusDto(
                "NOT_GENERATED",
                currentProfileVersion,
                null,
                null
        ));
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
