package com.dsaplatform.service.impl;

import com.dsaplatform.dto.auth.AuthResponse;
import com.dsaplatform.dto.auth.LoginRequest;
import com.dsaplatform.dto.auth.RefreshTokenRequest;
import com.dsaplatform.dto.auth.RegisterRequest;
import com.dsaplatform.dto.user.UserProfileResponse;
import com.dsaplatform.entity.User;
import com.dsaplatform.entity.enums.Role;
import com.dsaplatform.exception.BadRequestException;
import com.dsaplatform.exception.UnauthorizedException;
import com.dsaplatform.repository.UserRepository;
import com.dsaplatform.security.JwtTokenProvider;
import com.dsaplatform.security.UserPrincipal;
import com.dsaplatform.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        String username = request.getUsername().trim();
        String email = request.getEmail().trim().toLowerCase();

        if (userRepository.existsByUsername(username)) {
            throw new BadRequestException("Username '" + username + "' is already taken");
        }

        if (userRepository.existsByEmail(email)) {
            throw new BadRequestException("Email '" + email + "' is already registered");
        }

        String displayName = request.getDisplayName() != null && !request.getDisplayName().isBlank()
                ? request.getDisplayName().trim()
                : username;

        User user = User.builder()
                .username(username)
                .email(email)
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .displayName(displayName)
                .enabled(true)
                .lastLoginAt(Instant.now())
                .build();

        User savedUser = userRepository.save(user);
        UserPrincipal principal = UserPrincipal.create(savedUser);

        String accessToken = tokenProvider.generateAccessToken(principal);
        String refreshToken = tokenProvider.generateRefreshToken(principal);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresInMs(tokenProvider.getAccessTokenExpirationMs())
                .user(UserProfileResponse.fromEntity(savedUser))
                .build();
    }

    @Override
    @Transactional
    public AuthResponse login(LoginRequest request) {
        String identifier = request.getUsernameOrEmail().trim();

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(identifier, request.getPassword())
        );

        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        User user = userRepository.findById(principal.getId())
                .orElseThrow(() -> new UnauthorizedException("User not found"));

        user.setLastLoginAt(Instant.now());
        userRepository.save(user);

        String accessToken = tokenProvider.generateAccessToken(principal);
        String refreshToken = tokenProvider.generateRefreshToken(principal);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresInMs(tokenProvider.getAccessTokenExpirationMs())
                .user(UserProfileResponse.fromEntity(user))
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponse refresh(RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();

        if (!tokenProvider.validateToken(refreshToken)) {
            throw new UnauthorizedException("Invalid or expired refresh token");
        }

        String tokenType = tokenProvider.getTokenTypeFromToken(refreshToken);
        if (!"REFRESH".equals(tokenType)) {
            throw new UnauthorizedException("Provided token is not a refresh token");
        }

        UUID userId = tokenProvider.getUserIdFromToken(refreshToken);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UnauthorizedException("User not found"));

        if (!user.isEnabled()) {
            throw new UnauthorizedException("User account is disabled");
        }

        UserPrincipal principal = UserPrincipal.create(user);
        String newAccessToken = tokenProvider.generateAccessToken(principal);
        String newRefreshToken = tokenProvider.generateRefreshToken(principal);

        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .tokenType("Bearer")
                .expiresInMs(tokenProvider.getAccessTokenExpirationMs())
                .user(UserProfileResponse.fromEntity(user))
                .build();
    }
}
