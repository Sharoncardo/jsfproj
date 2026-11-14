package com.dsaplatform;

import com.dsaplatform.entity.*;
import com.dsaplatform.entity.enums.Difficulty;
import com.dsaplatform.entity.enums.Topic;
import com.dsaplatform.repository.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ProblemCatalogIntegrationTest {

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private ProblemRepository problemRepository;

    @Autowired
    private HintRepository hintRepository;

    @Autowired
    private LanguageOptionRepository languageOptionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProgressTrackRepository progressTrackRepository;

    @Autowired
    private BookmarkRepository bookmarkRepository;

    @Autowired
    private UserHintProgressRepository userHintProgressRepository;

    @Autowired
    private UserProblemRevisionRepository userProblemRevisionRepository;

    @Test
    @DisplayName("Verify that all 10 standard DSA topics are seeded correctly")
    void testTopicsSeeded() {
        List<TopicEntity> topics = topicRepository.findAllByOrderByDisplayOrderAsc();
        assertEquals(10, topics.size(), "Should have exactly 10 topics");

        Optional<TopicEntity> arraysTopic = topicRepository.findByCode("ARRAYS");
        assertTrue(arraysTopic.isPresent());
        assertEquals("Arrays & Hashing", arraysTopic.get().getName());
        assertEquals("arrays", arraysTopic.get().getSlug());
    }

    @Test
    @DisplayName("Verify problem catalog count and verified LeetCode 42 Trapping Rain Water as Hard")
    void testProblemCatalogSeeded() {
        List<Problem> problems = problemRepository.findAll();
        assertEquals(20, problems.size(), "Should have exactly 20 verified problems seeded");

        // Verify LeetCode 42 Trapping Rain Water
        Optional<Problem> trappingOpt = problemRepository.findBySlug("trapping-rain-water");
        assertTrue(trappingOpt.isPresent(), "Trapping Rain Water must be present");
        Problem trapping = trappingOpt.get();
        assertEquals("Trapping Rain Water", trapping.getTitle());
        assertEquals(Difficulty.HARD, trapping.getDifficulty(), "Trapping Rain Water must have verified HARD difficulty");
        assertEquals(42, trapping.getLeetcodeNumber());
        assertEquals(Topic.ARRAYS, trapping.getTopicCode());
    }

    @Test
    @DisplayName("Verify that EVERY single problem has EXACTLY four concise progressive hints")
    void testExactlyFourHintsPerProblem() {
        List<Problem> problems = problemRepository.findAll();
        assertFalse(problems.isEmpty());

        for (Problem problem : problems) {
            List<Hint> hints = hintRepository.findByProblemIdOrderByLevelAsc(problem.getId());
            assertEquals(4, hints.size(), "Problem " + problem.getSlug() + " must have exactly 4 hints");
            assertEquals(1, hints.get(0).getLevel());
            assertEquals(2, hints.get(1).getLevel());
            assertEquals(3, hints.get(2).getLevel());
            assertEquals(4, hints.get(3).getLevel());

            // Verify hint content is concise and not empty
            for (Hint hint : hints) {
                assertNotNull(hint.getTitle());
                assertNotNull(hint.getContent());
                assertFalse(hint.getContent().trim().isEmpty());
            }
        }
    }

    @Test
    @DisplayName("Verify starter templates contain only boilerplate and do not expose complete solutions")
    void testBoilerplateStarterTemplates() {
        Optional<Problem> twoSumOpt = problemRepository.findBySlug("two-sum");
        assertTrue(twoSumOpt.isPresent());

        List<LanguageOption> options = languageOptionRepository.findByProblemId(twoSumOpt.get().getId());
        assertEquals(3, options.size(), "Should have Java, Python, and C++ starter templates");

        for (LanguageOption opt : options) {
            String template = opt.getStarterTemplate();
            assertNotNull(template);
            assertTrue(template.contains("class Solution"), "Should have class Solution boilerplate");
            // Must NOT contain complete working solution logic
            assertFalse(template.contains("complement = target"), "Should not expose full solution logic");
            assertFalse(template.contains("map.put"), "Should not expose full solution logic");
        }
    }

    @Test
    @DisplayName("Verify user hint progress persistence for an authenticated user")
    void testUserHintProgressPersistence() {
        User user = userRepository.findByUsername("demo_user")
                .orElseThrow(() -> new AssertionError("demo_user should exist from V2"));
        Problem problem = problemRepository.findBySlug("two-sum")
                .orElseThrow(() -> new AssertionError("two-sum should exist"));

        UserHintProgress progress = UserHintProgress.builder()
                .user(user)
                .problem(problem)
                .unlockedLevel(2)
                .build();

        UserHintProgress saved = userHintProgressRepository.save(progress);
        assertNotNull(saved.getId());

        Optional<UserHintProgress> fetched = userHintProgressRepository.findByUserIdAndProblemId(user.getId(), problem.getId());
        assertTrue(fetched.isPresent());
        assertEquals(2, fetched.get().getUnlockedLevel());
    }

    @Test
    @DisplayName("Verify user problem revision (playbook notes) persistence")
    void testUserProblemRevisionPersistence() {
        User user = userRepository.findByUsername("demo_user")
                .orElseThrow(() -> new AssertionError("demo_user should exist from V2"));
        Problem problem = problemRepository.findBySlug("trapping-rain-water")
                .orElseThrow(() -> new AssertionError("trapping-rain-water should exist"));

        UserProblemRevision revision = UserProblemRevision.builder()
                .user(user)
                .problem(problem)
                .personalMnemonic("Two-pointer inward squeeze")
                .personalApproach("Maintain leftMax and rightMax, advance smaller wall")
                .reasoning("Bottleneck is determined by lower wall")
                .timeComplexity("O(N)")
                .timeComplexityExplanation("Each element visited once by either left or right pointer")
                .spaceComplexity("O(1)")
                .spaceComplexityExplanation("Only scalar pointers and running maxes maintained")
                .build();

        UserProblemRevision saved = userProblemRevisionRepository.save(revision);
        assertNotNull(saved.getId());

        Optional<UserProblemRevision> fetched = userProblemRevisionRepository.findByUserIdAndProblemId(user.getId(), problem.getId());
        assertTrue(fetched.isPresent());
        assertEquals("Two-pointer inward squeeze", fetched.get().getPersonalMnemonic());
    }

    @Test
    @DisplayName("Verify existing user accounts, progress tracks, and bookmarks are 100% preserved")
    void testExistingDataPreserved() {
        // Users from V2
        Optional<User> demoUser = userRepository.findByUsername("demo_user");
        assertTrue(demoUser.isPresent(), "demo_user must be preserved");
        Optional<User> adminUser = userRepository.findByUsername("admin");
        assertTrue(adminUser.isPresent(), "admin user must be preserved");

        // Progress tracks from V2 (16 seeded tracks)
        List<ProgressTrack> tracks = progressTrackRepository.findAllByUserId(demoUser.get().getId());
        assertEquals(16, tracks.size(), "All 16 seeded progress tracks must be preserved");

        // Bookmarks from V2 (3 seeded bookmarks)
        List<Bookmark> bookmarks = bookmarkRepository.findAllByUserIdOrderByCreatedAtDesc(demoUser.get().getId());
        assertEquals(3, bookmarks.size(), "All 3 seeded bookmarks must be preserved");
    }
}
