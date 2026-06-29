package com.placement.platform.service;

import com.placement.platform.entity.InterviewProfile;
import com.placement.platform.entity.User;

public interface InterviewProfileService {
    InterviewProfile getOrCreateProfile(User user);
    void incrementProfileVersion(User user);
}
