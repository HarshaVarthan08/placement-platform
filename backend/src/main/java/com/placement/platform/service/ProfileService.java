package com.placement.platform.service;

import com.placement.platform.dto.ProfileResponseDto;
import com.placement.platform.dto.UpdateProfileRequestDto;

public interface ProfileService {
    ProfileResponseDto getCurrentUserProfile();
    ProfileResponseDto updateProfile(UpdateProfileRequestDto request);
}
