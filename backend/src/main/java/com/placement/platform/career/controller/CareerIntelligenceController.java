package com.placement.platform.career.controller;

import com.placement.platform.career.intelligence.CareerIntelligenceProfile;
import com.placement.platform.career.service.CareerIntelligenceService;
import com.placement.platform.entity.User;
import com.placement.platform.exception.UserNotFoundException;
import com.placement.platform.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/career")
@Tag(name = "Career Intelligence", description = "Endpoints for career pathway recommendation analysis and preparation planning")
public class CareerIntelligenceController {

    private final CareerIntelligenceService careerIntelligenceService;
    private final UserRepository userRepository;

    public CareerIntelligenceController(
            CareerIntelligenceService careerIntelligenceService,
            UserRepository userRepository
    ) {
        this.careerIntelligenceService = careerIntelligenceService;
        this.userRepository = userRepository;
    }

    @GetMapping("/intelligence/{recommendationId}")
    @Operation(summary = "Get career intelligence profile", description = "Retrieves the personalized career analysis (difficulty, estimated preparation weeks, match confidence breakdown) for a specific job recommendation.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully retrieved career intelligence profile details"),
        @ApiResponse(responseCode = "404", description = "Job recommendation not found or user not authorized")
    })
    public ResponseEntity<CareerIntelligenceProfile> getCareerIntelligenceProfile(@PathVariable Long recommendationId) {
        User user = getAuthenticatedUser();
        CareerIntelligenceProfile profile = careerIntelligenceService.getProfile(recommendationId, user);
        return ResponseEntity.ok(profile);
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
