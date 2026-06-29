package com.placement.platform.service;

import com.placement.platform.dto.InterviewAvailabilityResponseDto;
import com.placement.platform.dto.InterviewSessionResponseDto;
import com.placement.platform.dto.StartInterviewRequestDto;
import com.placement.platform.entity.*;
import com.placement.platform.exception.InterviewCooldownException;
import com.placement.platform.exception.UserNotFoundException;
import com.placement.platform.mapper.InterviewMapper;
import com.placement.platform.repository.InterviewProfileRepository;
import com.placement.platform.repository.InterviewSessionRepository;
import com.placement.platform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class InterviewServiceImpl implements InterviewService {

    private final UserRepository userRepository;
    private final InterviewSessionRepository interviewSessionRepository;
    private final InterviewProfileRepository interviewProfileRepository;
    private final InterviewMapper interviewMapper;

    @Value("${application.interview.cooldown-hours:12}")
    private int cooldownHours;

    @Value("${application.interview.learning.global-ratio:60}")
    private int globalRatio;

    @Value("${application.interview.learning.personalized-ratio:40}")
    private int personalizedRatio;

    @Value("${application.interview.default-total-questions:5}")
    private int defaultTotalQuestions;

    public InterviewServiceImpl(
            UserRepository userRepository,
            InterviewSessionRepository interviewSessionRepository,
            InterviewProfileRepository interviewProfileRepository,
            InterviewMapper interviewMapper) {
        this.userRepository = userRepository;
        this.interviewSessionRepository = interviewSessionRepository;
        this.interviewProfileRepository = interviewProfileRepository;
        this.interviewMapper = interviewMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public InterviewAvailabilityResponseDto checkAvailability() {
        User user = getAuthenticatedUser();
        Optional<InterviewSession> latestMockSessionOpt = interviewSessionRepository
                .findFirstByUserAndModeOrderByStartedAtDesc(user, InterviewMode.MOCK);

        if (latestMockSessionOpt.isEmpty()) {
            return new InterviewAvailabilityResponseDto(true, null, 0, 0);
        }

        InterviewSession latestSession = latestMockSessionOpt.get();

        // If the latest mock session is still in progress, the user cannot start a new one.
        if (latestSession.getStatus() == InterviewStatus.IN_PROGRESS) {
            return new InterviewAvailabilityResponseDto(false, null, 0, 0);
        }

        LocalDateTime cooldownUntil = latestSession.getCooldownUntil();

        if (cooldownUntil == null || LocalDateTime.now().isAfter(cooldownUntil)) {
            return new InterviewAvailabilityResponseDto(true, null, 0, 0);
        }

        Duration duration = Duration.between(LocalDateTime.now(), cooldownUntil);
        long remainingHours = duration.toHours();
        long remainingMinutes = duration.toMinutes() % 60;

        if (remainingHours < 0) remainingHours = 0;
        if (remainingMinutes < 0) remainingMinutes = 0;

        return new InterviewAvailabilityResponseDto(false, cooldownUntil, remainingHours, remainingMinutes);
    }

    @Override
    @Transactional
    public InterviewSessionResponseDto startInterview(StartInterviewRequestDto request) {
        User user = getAuthenticatedUser();

        // Prevent concurrent active sessions regardless of mode
        boolean hasActiveSession = interviewSessionRepository.existsByUserAndStatus(user, InterviewStatus.IN_PROGRESS);
        if (hasActiveSession) {
            throw new InterviewCooldownException("You already have an active interview session in progress. Please complete or cancel it first.");
        }

        if (request.mode() == InterviewMode.MOCK) {
            InterviewAvailabilityResponseDto availability = checkAvailability();
            if (!availability.available()) {
                if (availability.nextAvailableAt() == null) {
                    throw new InterviewCooldownException("You already have an active mock interview session in progress.");
                } else {
                    throw new InterviewCooldownException("Mock interview is on cooldown until " + availability.nextAvailableAt() 
                            + ". Remaining: " + availability.remainingHours() + " hours, " + availability.remainingMinutes() + " minutes.");
                }
            }
        }

        // Default difficulty to MEDIUM if not specified
        InterviewDifficulty difficulty = request.difficulty() != null ? request.difficulty() : InterviewDifficulty.MEDIUM;

        // Retrieve the current profile version used for lazy regeneration
        int profileVersion = interviewProfileRepository.findByUser(user)
                .map(InterviewProfile::getProfileVersion)
                .orElse(1);

        InterviewSession session = new InterviewSession();
        session.setUser(user);
        session.setMode(request.mode());
        session.setStatus(InterviewStatus.IN_PROGRESS);
        session.setProfileVersionUsed(profileVersion);
        session.setTotalQuestions(defaultTotalQuestions);
        session.setStartedAt(LocalDateTime.now());

        // Note: completedAt and cooldownUntil remain null until completion in later milestones.

        InterviewSession savedSession = interviewSessionRepository.save(session);
        return interviewMapper.toDto(savedSession);
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
