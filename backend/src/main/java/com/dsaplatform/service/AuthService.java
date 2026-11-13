package com.dsaplatform.service;

import com.dsaplatform.dto.auth.AuthResponse;
import com.dsaplatform.dto.auth.LoginRequest;
import com.dsaplatform.dto.auth.RefreshTokenRequest;
import com.dsaplatform.dto.auth.RegisterRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
    AuthResponse refresh(RefreshTokenRequest request);
}
