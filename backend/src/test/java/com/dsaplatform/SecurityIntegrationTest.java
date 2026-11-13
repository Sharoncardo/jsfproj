package com.dsaplatform;

import com.dsaplatform.entity.StickyNote;
import com.dsaplatform.entity.User;
import com.dsaplatform.entity.enums.NoteType;
import com.dsaplatform.entity.enums.Role;
import com.dsaplatform.repository.StickyNoteRepository;
import com.dsaplatform.repository.UserRepository;
import com.dsaplatform.security.JwtTokenProvider;
import com.dsaplatform.security.UserPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StickyNoteRepository stickyNoteRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider tokenProvider;

    private User userAlice;
    private User userBob;
    private String aliceToken;
    private String bobToken;

    @BeforeEach
    void setup() {
        stickyNoteRepository.deleteAll();

        userAlice = userRepository.findByUsername("alice").orElseGet(() ->
                userRepository.save(User.builder()
                        .username("alice")
                        .email("alice@example.com")
                        .passwordHash(passwordEncoder.encode("Password123!"))
                        .role(Role.USER)
                        .displayName("Alice")
                        .enabled(true)
                        .build())
        );

        userBob = userRepository.findByUsername("bob").orElseGet(() ->
                userRepository.save(User.builder()
                        .username("bob")
                        .email("bob@example.com")
                        .passwordHash(passwordEncoder.encode("Password123!"))
                        .role(Role.USER)
                        .displayName("Bob")
                        .enabled(true)
                        .build())
        );

        aliceToken = tokenProvider.generateAccessToken(UserPrincipal.create(userAlice));
        bobToken = tokenProvider.generateAccessToken(UserPrincipal.create(userBob));
    }

    @Test
    @DisplayName("Unauthenticated request to secured endpoint /api/users/me returns 401 JSON")
    void getMe_Unauthenticated_Returns401() throws Exception {
        mockMvc.perform(get("/api/users/me"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.error").value("Unauthorized"));
    }

    @Test
    @DisplayName("Authenticated request to /api/users/me with valid Bearer token returns 200 OK")
    void getMe_Authenticated_Returns200() throws Exception {
        mockMvc.perform(get("/api/users/me")
                        .header("Authorization", "Bearer " + aliceToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.username").value("alice"))
                .andExpect(jsonPath("$.data.email").value("alice@example.com"));
    }

    @Test
    @DisplayName("Public endpoint /api/auth/login works without credentials header")
    void login_PublicEndpoint_Reachable() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"usernameOrEmail\":\"alice\",\"password\":\"Password123!\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.accessToken").isNotEmpty());
    }

    @Test
    @DisplayName("User cannot delete another user's sticky note -> returns 403 Forbidden")
    void deleteNote_OtherUserResource_Returns403() throws Exception {
        // Create a sticky note owned by Alice
        StickyNote aliceNote = stickyNoteRepository.save(StickyNote.builder()
                .user(userAlice)
                .title("Alice's Secret Note")
                .content("Do not touch")
                .type(NoteType.NOTE)
                .build());

        // Bob attempts to delete Alice's note
        mockMvc.perform(delete("/api/notes/" + aliceNote.getId())
                        .header("Authorization", "Bearer " + bobToken))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(403))
                .andExpect(jsonPath("$.error").value("Forbidden"));
    }
}
