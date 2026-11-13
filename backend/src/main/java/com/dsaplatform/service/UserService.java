package com.dsaplatform.service;

import com.dsaplatform.dto.user.UpdateProfileRequest;
import com.dsaplatform.dto.user.UserProfileResponse;

import java.util.UUID;

public interface UserService {
    UserProfileResponse getCurrentUserProfile(UUID userId);
    UserProfileResponse updateProfile(UUID userId, UpdateProfileRequest request);
}
