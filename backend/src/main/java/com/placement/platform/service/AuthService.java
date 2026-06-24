package com.placement.platform.service;

import com.placement.platform.dto.AuthResponse;
import com.placement.platform.dto.LoginRequest;
import com.placement.platform.dto.MessageResponse;
import com.placement.platform.dto.RegisterRequest;

public interface AuthService {
    MessageResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}
