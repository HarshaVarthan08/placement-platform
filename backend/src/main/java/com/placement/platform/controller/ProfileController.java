package com.placement.platform.controller;

import com.placement.platform.dto.ProfileResponseDto;
import com.placement.platform.dto.UpdateProfileRequestDto;
import com.placement.platform.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/profile")
@Tag(name = "Profile", description = "Endpoints for managing user candidate profile details")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/me")
    @Operation(summary = "Get current user profile", description = "Retrieves the authenticated user's profile metadata and information details.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved candidate profile")
    public ResponseEntity<ProfileResponseDto> getCurrentUserProfile() {
        ProfileResponseDto profile = profileService.getCurrentUserProfile();
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/me")
    @Operation(summary = "Update current user profile", description = "Updates candidate profile information (target role, graduation year, CGPA, target companies). Increments profile version.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Profile successfully updated"),
        @ApiResponse(responseCode = "400", description = "Invalid request payload")
    })
    public ResponseEntity<ProfileResponseDto> updateProfile(@Valid @RequestBody UpdateProfileRequestDto request) {
        ProfileResponseDto updatedProfile = profileService.updateProfile(request);
        return ResponseEntity.ok(updatedProfile);
    }
}
