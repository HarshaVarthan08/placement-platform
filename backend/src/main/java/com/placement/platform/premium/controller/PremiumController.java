package com.placement.platform.premium.controller;

import com.placement.platform.entity.User;
import com.placement.platform.exception.UserNotFoundException;
import com.placement.platform.premium.config.PremiumProperties;
import com.placement.platform.premium.dto.*;
import com.placement.platform.premium.entity.PremiumWaitlist;
import com.placement.platform.premium.enums.PremiumFeature;
import com.placement.platform.premium.repository.PremiumWaitlistRepository;
import com.placement.platform.premium.service.FeatureAvailabilityService;
import com.placement.platform.premium.service.PremiumAccessService;
import com.placement.platform.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/premium")
public class PremiumController {

    private static final Logger log = LoggerFactory.getLogger(PremiumController.class);

    private final PremiumAccessService premiumAccessService;
    private final FeatureAvailabilityService availabilityService;
    private final PremiumWaitlistRepository waitlistRepository;
    private final UserRepository userRepository;
    private final PremiumProperties premiumProperties;

    public PremiumController(
            PremiumAccessService premiumAccessService,
            FeatureAvailabilityService availabilityService,
            PremiumWaitlistRepository waitlistRepository,
            UserRepository userRepository,
            PremiumProperties premiumProperties
    ) {
        this.premiumAccessService = premiumAccessService;
        this.availabilityService = availabilityService;
        this.waitlistRepository = waitlistRepository;
        this.userRepository = userRepository;
        this.premiumProperties = premiumProperties;
    }

    @GetMapping("/catalog")
    public ResponseEntity<List<PremiumFeatureDto>> getCatalog() {
        List<PremiumFeatureDto> dtos = Arrays.stream(PremiumFeature.values())
                .map(f -> {
                    FeatureAvailability avail = availabilityService.getAvailability(f);
                    return new PremiumFeatureDto(
                            f.getDisplayName(),
                            f.name(),
                            true,
                            avail.status(),
                            avail.launchQuarter(),
                            f.getDescription(),
                            f.getCategory(),
                            f.getIconKey()
                    );
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PostMapping("/waitlist")
    public ResponseEntity<Map<String, Object>> joinWaitlist(@RequestBody WaitlistRequestDto request) {
        if (!premiumProperties.isWaitlistEnabled()) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Waitlist is currently disabled.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        User user = getAuthenticatedUser();
        PremiumFeature feature;
        try {
            feature = PremiumFeature.valueOf(request.feature());
        } catch (IllegalArgumentException | NullPointerException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Invalid premium feature: " + request.feature());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        if (waitlistRepository.existsByUserIdAndFeature(user.getId(), feature)) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "You are already registered on the waitlist for " + feature.getDisplayName() + ".");
            return ResponseEntity.ok(response);
        }

        PremiumWaitlist waitlist = new PremiumWaitlist(user, feature, request.source());
        waitlistRepository.save(waitlist);

        log.info("Waitlist registration. User:{}. Feature:{}. Source:{}", user.getId(), feature, request.source());

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Successfully registered for the " + feature.getDisplayName() + " waitlist.");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/resume-optimizer")
    public ResponseEntity<PremiumPlaceholderResponse> resumeOptimizer() {
        return getPlaceholderResponse(PremiumFeature.AI_RESUME_OPTIMIZER);
    }

    @PostMapping("/career-coach")
    public ResponseEntity<PremiumPlaceholderResponse> careerCoach() {
        return getPlaceholderResponse(PremiumFeature.AI_CAREER_COACH);
    }

    @PostMapping("/interview-coach")
    public ResponseEntity<PremiumPlaceholderResponse> interviewCoach() {
        return getPlaceholderResponse(PremiumFeature.AI_INTERVIEW_COACH);
    }

    private ResponseEntity<PremiumPlaceholderResponse> getPlaceholderResponse(PremiumFeature feature) {
        User user = getAuthenticatedUser();
        boolean isPremium = premiumAccessService.isPremium(user);

        if (!isPremium) {
            PremiumPlaceholderResponse freeResponse = new PremiumPlaceholderResponse(
                    false,
                    "COMING_SOON",
                    true,
                    "This Premium feature is not yet available. Join the early access list to be notified when it launches."
            );
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(freeResponse);
        } else {
            PremiumPlaceholderResponse premiumResponse = new PremiumPlaceholderResponse(
                    false,
                    "NOT_IMPLEMENTED",
                    null,
                    "Premium feature architecture is ready. AI implementation will be available in Sprint 11."
            );
            return ResponseEntity.ok(premiumResponse);
        }
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
