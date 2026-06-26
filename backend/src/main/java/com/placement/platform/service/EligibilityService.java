package com.placement.platform.service;

import com.placement.platform.dto.EligibilityResponseDto;
import com.placement.platform.entity.TargetCompany;
import com.placement.platform.entity.User;

public interface EligibilityService {
    EligibilityResponseDto checkEligibility(User user, TargetCompany company);
}
