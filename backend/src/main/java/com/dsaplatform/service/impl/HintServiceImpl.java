package com.dsaplatform.service.impl;

import com.dsaplatform.dto.hint.HintDto;
import com.dsaplatform.dto.hint.HintProgressDto;
import com.dsaplatform.entity.Hint;
import com.dsaplatform.entity.Problem;
import com.dsaplatform.entity.User;
import com.dsaplatform.entity.UserHintProgress;
import com.dsaplatform.exception.ResourceNotFoundException;
import com.dsaplatform.repository.HintRepository;
import com.dsaplatform.repository.ProblemRepository;
import com.dsaplatform.repository.UserHintProgressRepository;
import com.dsaplatform.repository.UserRepository;
import com.dsaplatform.service.HintService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class HintServiceImpl implements HintService {

    private final HintRepository hintRepository;
    private final ProblemRepository problemRepository;
    private final UserHintProgressRepository userHintProgressRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public HintProgressDto getHintProgress(UUID userId, Long problemId) {
        Problem problem = validateProblemExists(problemId);
        int unlockedLevel = userHintProgressRepository
                .findByUserIdAndProblemId(userId, problemId)
                .map(UserHintProgress::getUnlockedLevel)
                .orElse(0);

        return buildProgressDto(problem, unlockedLevel);
    }

    @Override
    @Transactional
    public HintProgressDto unlockNextHint(UUID userId, Long problemId) {
        Problem problem = validateProblemExists(problemId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        UserHintProgress progress = userHintProgressRepository
                .findByUserIdAndProblemId(userId, problemId)
                .orElseGet(() -> UserHintProgress.builder()
                        .user(user)
                        .problem(problem)
                        .unlockedLevel(0)
                        .build());

        if (progress.getUnlockedLevel() < 4) {
            progress.setUnlockedLevel(progress.getUnlockedLevel() + 1);
            userHintProgressRepository.save(progress);
        }

        return buildProgressDto(problem, progress.getUnlockedLevel());
    }

    @Override
    @Transactional
    public HintProgressDto resetHintProgress(UUID userId, Long problemId) {
        Problem problem = validateProblemExists(problemId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        UserHintProgress progress = userHintProgressRepository
                .findByUserIdAndProblemId(userId, problemId)
                .orElseGet(() -> UserHintProgress.builder()
                        .user(user)
                        .problem(problem)
                        .unlockedLevel(0)
                        .build());

        progress.setUnlockedLevel(0);
        userHintProgressRepository.save(progress);

        return buildProgressDto(problem, 0);
    }

    private HintProgressDto buildProgressDto(Problem problem, int unlockedLevel) {
        List<Hint> hints = hintRepository.findByProblemIdOrderByLevelAsc(problem.getId());
        List<HintDto> hintDtos = new ArrayList<>();

        for (Hint h : hints) {
            boolean isUnlocked = h.getLevel() <= unlockedLevel;
            hintDtos.add(HintDto.builder()
                    .id(h.getId())
                    .level(h.getLevel())
                    .title(h.getTitle())
                    // Mask content completely if locked to prevent client-side inspection
                    .content(isUnlocked ? h.getContent() : null)
                    .timeComplexity(isUnlocked && h.getLevel() == 4 ? h.getTimeComplexity() : null)
                    .spaceComplexity(isUnlocked && h.getLevel() == 4 ? h.getSpaceComplexity() : null)
                    .unlocked(isUnlocked)
                    .build());
        }

        boolean hasMore = unlockedLevel < 4;
        Integer nextLevel = hasMore ? (unlockedLevel + 1) : null;
        String message = (unlockedLevel == 0)
                ? "No hints unlocked yet. Click 'Show Next Hint' to reveal Level 1 (Conceptual Nudge)."
                : (hasMore
                    ? "Unlocked " + unlockedLevel + " of 4 progressive hints."
                    : "All 4 progressive hints have been unlocked!");

        return HintProgressDto.builder()
                .problemId(problem.getId())
                .unlockedLevel(unlockedLevel)
                .totalHints(4)
                .hasMoreHints(hasMore)
                .nextLevel(nextLevel)
                .hints(hintDtos)
                .message(message)
                .build();
    }

    private Problem validateProblemExists(Long problemId) {
        return problemRepository.findById(problemId)
                .orElseThrow(() -> new ResourceNotFoundException("Problem", "id", problemId));
    }
}
