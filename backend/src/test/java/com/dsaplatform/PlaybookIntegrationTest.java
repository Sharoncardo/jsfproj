
package com.dsaplatform;

import com.dsaplatform.dto.playbook.PlaybookProblemDetailDto;
import com.dsaplatform.dto.playbook.PlaybookResponse;
import com.dsaplatform.entity.*;
import com.dsaplatform.entity.enums.ProgressStatus;
import com.dsaplatform.repository.*;
import com.dsaplatform.service.PlaybookService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class PlaybookIntegrationTest {

    @Autowired
    private PlaybookService playbookService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProblemRepository problemRepository;

    @Autowired
    private ProgressTrackRepository progressTrackRepository;

    @Autowired
    private UserHintProgressRepository userHintProgressRepository;

    @Autowired
    private UserProblemRevisionRepository userProblemRevisionRepository;

    @Test
    @DisplayName("Verify Playbook includes all four hints and personal revision notes")
    void testPlaybookContainsHintsAndRevision() {

        // 1. Get the seeded test user.
        User user = userRepository.findByUsername("demo_user")
                .orElseThrow(() -> new AssertionError("demo_user should exist"));

        // 2. Get a problem from the catalogue.
        Problem problem = problemRepository.findBySlug("two-sum")
                .orElseThrow(() -> new AssertionError("two-sum should exist"));

        // 3. Create or update this user's progress.
        ProgressTrack progress = progressTrackRepository
                .findByUserIdAndProblemId(user.getId(), problem.getSlug())
                .orElseGet(() -> ProgressTrack.builder()
                        .user(user)
                        .problemId(problem.getSlug())
                        .topic(problem.getTopicCode())
                        .difficulty(problem.getDifficulty())
                        .build());

        progress.setStatus(ProgressStatus.IN_PROGRESS);
        progress.setAttempts(1);
        progressTrackRepository.save(progress);

        // 4. Set the unlocked hint level to 2.
        UserHintProgress hintProgress = userHintProgressRepository
                .findByUserIdAndProblemId(user.getId(), problem.getId())
                .orElseGet(() -> UserHintProgress.builder()
                        .user(user)
                        .problem(problem)
                        .build());

        hintProgress.setUnlockedLevel(2);
        userHintProgressRepository.save(hintProgress);

        // 5. Save the student's personal revision.
        UserProblemRevision revision = userProblemRevisionRepository
                .findByUserIdAndProblemId(user.getId(), problem.getId())
                .orElseGet(() -> UserProblemRevision.builder()
                        .user(user)
                        .problem(problem)
                        .build());

        revision.setPersonalMnemonic("HashMap remembers the complement");
        revision.setPersonalApproach("Store visited numbers and check the complement.");
        revision.setReasoning("Each number can be checked against its required complement.");
        revision.setTimeComplexity("O(N)");
        revision.setSpaceComplexity("O(N)");

        userProblemRevisionRepository.save(revision);

        // 6. Retrieve the Playbook.
        PlaybookResponse response = playbookService.getPlaybook(user.getId());

        assertNotNull(response);

        // 7. Find Two Sum in the returned patterns.
        PlaybookProblemDetailDto twoSum = response.getPatterns()
                .stream()
                .flatMap(pattern -> pattern.getProblems().stream())
                .filter(item -> item.getSlug().equals("two-sum"))
                .findFirst()
                .orElseThrow(() ->
                        new AssertionError("Two Sum should appear in the Playbook"));

        // 8. Verify progress.
        assertEquals(ProgressStatus.IN_PROGRESS, twoSum.getProgressStatus());
        assertEquals(1, twoSum.getAttempts());

        // 9. Verify all four hints are present.
        assertEquals(4, twoSum.getHints().size());

        // First two hints should be unlocked.
        assertTrue(twoSum.getHints().get(0).isUnlocked());
        assertTrue(twoSum.getHints().get(1).isUnlocked());

        // Last two hints should remain locked.
        assertFalse(twoSum.getHints().get(2).isUnlocked());
        assertFalse(twoSum.getHints().get(3).isUnlocked());

        // Playbook should contain all four hint contents.
        twoSum.getHints().forEach(hint ->
                assertNotNull(hint.getContent(),
                        "Playbook should include locked hint content too"));

        // 10. Verify personal revision.
        assertNotNull(twoSum.getRevision());
        assertEquals(
                "HashMap remembers the complement",
                twoSum.getRevision().getPersonalMnemonic()
        );

        assertEquals(
                "Store visited numbers and check the complement.",
                twoSum.getRevision().getPersonalApproach()
        );
    }
}
