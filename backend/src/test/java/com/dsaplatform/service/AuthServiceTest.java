package com.dsaplatform.service;

import com.dsaplatform.dto.auth.AuthResponse;
import com.dsaplatform.dto.auth.LoginRequest;
import com.dsaplatform.dto.auth.RefreshTokenRequest;
import com.dsaplatform.dto.auth.RegisterRequest;
import com.dsaplatform.entity.User;
import com.dsaplatform.entity.enums.Role;
import com.dsaplatform.exception.BadRequestException;
import com.dsaplatform.exception.UnauthorizedException;
import com.dsaplatform.repository.UserRepository;
import com.dsaplatform.security.JwtTokenProvider;
import com.dsaplatform.security.UserPrincipal;
import com.dsaplatform.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider tokenProvider;

    @InjectMocks
    private AuthServiceImpl authService;

    private User sampleUser;
    private UUID sampleUserId;

    @BeforeEach
    void setUp() {
        sampleUserId = UUID.randomUUID();
        sampleUser = User.builder()
                .id(sampleUserId)
                .username("testcoder")
                .email("testcoder@example.com")
                .passwordHash("hashedPass123")
                .role(Role.USER)
                .displayName("Test Coder")
                .enabled(true)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
    }

    @Test
    @DisplayName("register: Success when username and email are unique")
    void register_Success() {
        RegisterRequest request = RegisterRequest.builder()
                .username("testcoder")
                .email("testcoder@example.com")
                .password("SecurePass123!")
                .displayName("Test Coder")
                .build();

        when(userRepository.existsByUsername("testcoder")).thenReturn(false);
        when(userRepository.existsByEmail("testcoder@example.com")).thenReturn(false);
        when(passwordEncoder.encode("SecurePass123!")).thenReturn("hashedPass123");
        when(userRepository.save(any(User.class))).thenReturn(sampleUser);
        when(tokenProvider.generateAccessToken(any(UserPrincipal.class))).thenReturn("access.token.jwt");
        when(tokenProvider.generateRefreshToken(any(UserPrincipal.class))).thenReturn("refresh.token.jwt");
        when(tokenProvider.getAccessTokenExpirationMs()).thenReturn(900000L);

        AuthResponse response = authService.register(request);

        assertThat(response).isNotNull();
        assertThat(response.getAccessToken()).isEqualTo("access.token.jwt");
        assertThat(response.getRefreshToken()).isEqualTo("refresh.token.jwt");
        assertThat(response.getUser().getUsername()).isEqualTo("testcoder");
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("register: Throws BadRequestException when username is taken")
    void register_DuplicateUsername_ThrowsException() {
        RegisterRequest request = RegisterRequest.builder()
                .username("testcoder")
                .email("new@example.com")
                .password("SecurePass123!")
                .build();

        when(userRepository.existsByUsername("testcoder")).thenReturn(true);

        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Username 'testcoder' is already taken");

        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("login: Success with valid credentials")
    void login_Success() {
        LoginRequest request = LoginRequest.builder()
                .usernameOrEmail("testcoder")
                .password("SecurePass123!")
                .build();

        UserPrincipal principal = UserPrincipal.create(sampleUser);
        Authentication auth = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());

        when(authenticationManager.authenticate(any())).thenReturn(auth);
        when(userRepository.findById(sampleUserId)).thenReturn(Optional.of(sampleUser));
        when(userRepository.save(any(User.class))).thenReturn(sampleUser);
        when(tokenProvider.generateAccessToken(any(UserPrincipal.class))).thenReturn("access.token.jwt");
        when(tokenProvider.generateRefreshToken(any(UserPrincipal.class))).thenReturn("refresh.token.jwt");

        AuthResponse response = authService.login(request);

        assertThat(response).isNotNull();
        assertThat(response.getAccessToken()).isEqualTo("access.token.jwt");
        assertThat(response.getUser().getEmail()).isEqualTo("testcoder@example.com");
    }

    @Test
    @DisplayName("login: Fails with bad credentials")
    void login_BadCredentials_ThrowsException() {
        LoginRequest request = LoginRequest.builder()
                .usernameOrEmail("testcoder")
                .password("WrongPassword")
                .build();

        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(BadCredentialsException.class);
    }

    @Test
    @DisplayName("refresh: Success with valid refresh token")
    void refresh_Success() {
        RefreshTokenRequest request = new RefreshTokenRequest("valid.refresh.token");

        when(tokenProvider.validateToken("valid.refresh.token")).thenReturn(true);
        when(tokenProvider.getTokenTypeFromToken("valid.refresh.token")).thenReturn("REFRESH");
        when(tokenProvider.getUserIdFromToken("valid.refresh.token")).thenReturn(sampleUserId);
        when(userRepository.findById(sampleUserId)).thenReturn(Optional.of(sampleUser));
        when(tokenProvider.generateAccessToken(any(UserPrincipal.class))).thenReturn("new.access.token");
        when(tokenProvider.generateRefreshToken(any(UserPrincipal.class))).thenReturn("new.refresh.token");

        AuthResponse response = authService.refresh(request);

        assertThat(response.getAccessToken()).isEqualTo("new.access.token");
        assertThat(response.getRefreshToken()).isEqualTo("new.refresh.token");
    }

    @Test
    @DisplayName("refresh: Fails when refresh token is invalid")
    void refresh_InvalidToken_ThrowsException() {
        RefreshTokenRequest request = new RefreshTokenRequest("expired.token");

        when(tokenProvider.validateToken("expired.token")).thenReturn(false);

        assertThatThrownBy(() -> authService.refresh(request))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessageContaining("Invalid or expired refresh token");
    }
}
