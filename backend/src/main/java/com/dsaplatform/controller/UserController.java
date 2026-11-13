package com.dsaplatform.controller;

import com.dsaplatform.dto.common.ApiResponse;
import com.dsaplatform.dto.user.UpdateProfileRequest;
import com.dsaplatform.dto.user.UserProfileResponse;
import com.dsaplatform.security.UserPrincipal;
import com.dsaplatform.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User Profile", description = "Current authenticated user profile endpoints")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    @Operation(summary = "Get current authenticated user profile")
    public ResponseEntity<ApiResponse<UserProfileResponse>> getCurrentUser(
            @AuthenticationPrincipal UserPrincipal principal) {
        UserProfileResponse response = userService.getCurrentUserProfile(principal.getId());
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @PutMapping("/me")
    @Operation(summary = "Update current user profile (displayName, avatarUrl)")
    public ResponseEntity<ApiResponse<UserProfileResponse>> updateProfile(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody UpdateProfileRequest request) {
        UserProfileResponse response = userService.updateProfile(principal.getId(), request);
        return ResponseEntity.ok(ApiResponse.ok("Profile updated successfully", response));
    }
}
