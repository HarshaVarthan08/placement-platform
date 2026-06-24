package com.placement.platform.controller;

import com.placement.platform.dto.ProfileResponseDto;
import com.placement.platform.dto.UpdateProfileRequestDto;
import com.placement.platform.service.ProfileService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/me")
    public ResponseEntity<ProfileResponseDto> getCurrentUserProfile() {
        ProfileResponseDto profile = profileService.getCurrentUserProfile();
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/me")
    public ResponseEntity<ProfileResponseDto> updateProfile(@Valid @RequestBody UpdateProfileRequestDto request) {
        ProfileResponseDto updatedProfile = profileService.updateProfile(request);
        return ResponseEntity.ok(updatedProfile);
    }
}
