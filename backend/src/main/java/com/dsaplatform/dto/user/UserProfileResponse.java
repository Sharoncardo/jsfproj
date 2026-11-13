package com.dsaplatform.dto.user;

import com.dsaplatform.entity.User;
import com.dsaplatform.entity.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponse {
    private UUID id;
    private String username;
    private String email;
    private Role role;
    private String displayName;
    private String avatarUrl;
    private Instant createdAt;
    private Instant lastLoginAt;
    private boolean enabled;

    public static UserProfileResponse fromEntity(User user) {
        return UserProfileResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .displayName(user.getDisplayName())
                .avatarUrl(user.getAvatarUrl())
                .createdAt(user.getCreatedAt())
                .lastLoginAt(user.getLastLoginAt())
                .enabled(user.isEnabled())
                .build();
    }
}
