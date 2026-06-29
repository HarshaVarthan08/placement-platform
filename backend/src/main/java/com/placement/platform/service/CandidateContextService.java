package com.placement.platform.service;

import com.placement.platform.dto.CandidateContext;
import com.placement.platform.entity.User;

public interface CandidateContextService {
    CandidateContext buildContext(User user);
}
