package com.dsa.platform;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

import com.dsa.platform.hint.service.HintService;
import com.dsa.platform.hint.dto.HintProgressResponse;
import com.dsa.platform.problem.entity.Problem;
import com.dsa.platform.problem.repository.ProblemRepository;

@SpringBootTest
public class HintServiceTest {

    @Autowired
    private HintService hintService;

    @Autowired
    private ProblemRepository problemRepository;

    private Long testProblemId;
    private String testSessionId;

    @BeforeEach
    void setUp() {
        Problem problem = problemRepository.findBySlug("two-sum")
                .orElseThrow(() -> new IllegalStateException("Sample problem two-sum not found"));
        testProblemId = problem.getId();
        testSessionId = "test-session-" + UUID.randomUUID();
    }

    @Test
    void testInitialHintStateIsZero() {
        HintProgressResponse progress = hintService.getHintProgress(testProblemId, testSessionId);
        assertEquals(0, progress.getCurrentUnlockedLevel());
        assertEquals(4, progress.getTotalHints());
        assertTrue(progress.isHasMoreHints());
        assertEquals(1, progress.getNextLevel());
        assertTrue(progress.getUnlockedHints().isEmpty());
    }

    @Test
    void testProgressiveSequentialHintUnlocking() {
        // Unlock Level 1
        HintProgressResponse p1 = hintService.unlockNextHint(testProblemId, testSessionId);
        assertEquals(1, p1.getCurrentUnlockedLevel());
        assertEquals(1, p1.getUnlockedHints().size());
        assertEquals("Level 1: Conceptual Nudge", p1.getUnlockedHints().get(0).getTitle());
        assertTrue(p1.isHasMoreHints());
        assertEquals(2, p1.getNextLevel());

        // Unlock Level 2
        HintProgressResponse p2 = hintService.unlockNextHint(testProblemId, testSessionId);
        assertEquals(2, p2.getCurrentUnlockedLevel());
        assertEquals(2, p2.getUnlockedHints().size());
        assertEquals(2, p2.getUnlockedHints().get(1).getLevel());

        // Unlock Level 3
        HintProgressResponse p3 = hintService.unlockNextHint(testProblemId, testSessionId);
        assertEquals(3, p3.getCurrentUnlockedLevel());
        assertEquals(3, p3.getUnlockedHints().size());

        // Unlock Level 4
        HintProgressResponse p4 = hintService.unlockNextHint(testProblemId, testSessionId);
        assertEquals(4, p4.getCurrentUnlockedLevel());
        assertEquals(4, p4.getUnlockedHints().size());
        assertFalse(p4.isHasMoreHints());
        assertNull(p4.getNextLevel());
        assertNotNull(p4.getUnlockedHints().get(3).getTimeComplexity());
        assertNotNull(p4.getUnlockedHints().get(3).getSpaceComplexity());

        // Attempting to unlock beyond Level 4 remains at 4
        HintProgressResponse pExtra = hintService.unlockNextHint(testProblemId, testSessionId);
        assertEquals(4, pExtra.getCurrentUnlockedLevel());
        assertEquals(4, pExtra.getUnlockedHints().size());

        // Reset hint progress
        HintProgressResponse pReset = hintService.resetHintProgress(testProblemId, testSessionId);
        assertEquals(0, pReset.getCurrentUnlockedLevel());
        assertTrue(pReset.getUnlockedHints().isEmpty());
    }

    @Test
    void testLockedHintCannotBeRetrievedDirectly() {
        // Level 3 is locked initially
        assertThrows(IllegalStateException.class, () -> {
            hintService.getHintByLevel(testProblemId, 3, testSessionId);
        });
    }
}
