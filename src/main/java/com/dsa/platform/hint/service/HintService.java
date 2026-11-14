package com.dsa.platform.hint.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import com.dsa.platform.hint.entity.Hint;
import com.dsa.platform.hint.repository.HintRepository;
import com.dsa.platform.hint.dto.HintDTO;
import com.dsa.platform.hint.dto.HintProgressResponse;
import com.dsa.platform.problem.repository.ProblemRepository;

@Service
@Transactional(readOnly = true)
public class HintService {

    private final HintRepository hintRepository;
    private final ProblemRepository problemRepository;

    // In-memory session tracking for backend-controlled progressive hint unlocking
    // Key format: sessionId:problemId -> currentUnlockedLevel (0 to 4)
    private final Map<String, Integer> sessionProgressMap = new ConcurrentHashMap<>();

    public HintService(HintRepository hintRepository, ProblemRepository problemRepository) {
        this.hintRepository = hintRepository;
        this.problemRepository = problemRepository;
    }

    private String getProgressKey(Long problemId, String sessionId) {
        String sid = (sessionId != null && !sessionId.trim().isEmpty()) ? sessionId.trim() : "default-guest";
        return sid + ":" + problemId;
    }

    public int getCurrentUnlockedLevel(Long problemId, String sessionId) {
        return sessionProgressMap.getOrDefault(getProgressKey(problemId, sessionId), 0);
    }

    public HintProgressResponse getHintProgress(Long problemId, String sessionId) {
        validateProblemExists(problemId);
        int totalHints = (int) hintRepository.countByProblemId(problemId);
        int currentLevel = getCurrentUnlockedLevel(problemId, sessionId);

        List<Hint> allHints = hintRepository.findByProblemIdOrderByLevelAsc(problemId);
        List<HintDTO> unlockedList = allHints.stream()
                .filter(h -> h.getLevel() <= currentLevel)
                .map(this::toDTO)
                .collect(Collectors.toList());

        boolean hasMore = currentLevel < totalHints;
        Integer nextLevel = hasMore ? (currentLevel + 1) : null;

        String message = (currentLevel == 0)
                ? "No hints unlocked yet. Click 'Show Next Hint' to reveal Level 1 (Conceptual Nudge)."
                : (hasMore
                    ? "Unlocked " + currentLevel + " of " + totalHints + " progressive hints."
                    : "All " + totalHints + " progressive hints have been unlocked!");

        return new HintProgressResponse(
                problemId,
                currentLevel,
                totalHints > 0 ? totalHints : 4,
                totalHints,
                hasMore,
                nextLevel,
                unlockedList,
                message
        );
    }

    @Transactional
    public HintProgressResponse unlockNextHint(Long problemId, String sessionId) {
        validateProblemExists(problemId);
        int totalHints = (int) hintRepository.countByProblemId(problemId);
        String key = getProgressKey(problemId, sessionId);
        int currentLevel = sessionProgressMap.getOrDefault(key, 0);

        if (currentLevel >= totalHints) {
            return getHintProgress(problemId, sessionId);
        }

        // Increment unlocked level strictly sequentially
        int newLevel = currentLevel + 1;
        sessionProgressMap.put(key, newLevel);

        return getHintProgress(problemId, sessionId);
    }

    @Transactional
    public HintProgressResponse resetHintProgress(Long problemId, String sessionId) {
        validateProblemExists(problemId);
        String key = getProgressKey(problemId, sessionId);
        sessionProgressMap.put(key, 0);
        return getHintProgress(problemId, sessionId);
    }

    public HintDTO getHintByLevel(Long problemId, int level, String sessionId) {
        validateProblemExists(problemId);
        int currentLevel = getCurrentUnlockedLevel(problemId, sessionId);

        if (level > currentLevel) {
            throw new IllegalStateException("Hint level " + level + " is locked! You must unlock hints sequentially.");
        }

        Hint hint = hintRepository.findByProblemIdAndLevel(problemId, level)
                .orElseThrow(() -> new IllegalArgumentException("Hint level " + level + " not found for problem " + problemId));
        return toDTO(hint);
    }

    private void validateProblemExists(Long problemId) {
        if (!problemRepository.existsById(problemId)) {
            throw new IllegalArgumentException("Problem not found with id: " + problemId);
        }
    }

    public HintDTO toDTO(Hint h) {
        return new HintDTO(
                h.getId(),
                h.getLevel(),
                h.getTitle(),
                h.getContent(),
                h.getTimeComplexity(),
                h.getSpaceComplexity()
        );
    }
}
