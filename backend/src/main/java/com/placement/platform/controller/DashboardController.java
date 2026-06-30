package com.placement.platform.controller;

import com.placement.platform.dto.dashboard.DashboardResponseDto;
import com.placement.platform.entity.User;
import com.placement.platform.exception.UserNotFoundException;
import com.placement.platform.repository.UserRepository;
import com.placement.platform.service.dashboard.PlacementIntelligenceService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final PlacementIntelligenceService placementIntelligenceService;
    private final UserRepository userRepository;

    public DashboardController(
            PlacementIntelligenceService placementIntelligenceService,
            UserRepository userRepository
    ) {
        this.placementIntelligenceService = placementIntelligenceService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<DashboardResponseDto> getDashboard() {
        User user = getAuthenticatedUser();
        DashboardResponseDto response = placementIntelligenceService.getDashboardResponse(user);
        return ResponseEntity.ok(response);
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
