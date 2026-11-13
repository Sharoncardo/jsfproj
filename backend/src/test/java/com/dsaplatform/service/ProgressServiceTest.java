package com.dsaplatform.service;

import com.dsaplatform.dto.progress.ProgressSummaryResponse;
import com.dsaplatform.dto.progress.ProgressTrackDto;
import com.dsaplatform.dto.progress.RecordAttemptRequest;
import com.dsaplatform.entity.ProgressTrack;
import com.dsaplatform.entity.User;
import com.dsaplatform.entity.enums.Difficulty;
import com.dsaplatform.entity.enums.ProgressStatus;
import com.dsaplatform.entity.enums.Topic;
import com.dsaplatform.repository.ProgressTrackRepository;
import com.dsaplatform.repository.UserRepository;
import com.dsaplatform.service.impl.ProgressServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProgressServiceTest {

    @Mock
    private ProgressTrackRepository progressTrackRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ProgressServiceImpl progressService;

    private User sampleUser;
    private UUID sampleUserId;

    @BeforeEach
    void setUp() {
        sampleUserId = UUID.randomUUID();
        sampleUser = User.builder()
                .id(sampleUserId)
                .username("streakmaster")
                .email("streak@example.com")
                .build();
    }

    @Test
    @DisplayName("recordAttempt: Create new solved attempt sets solvedAt and increments attempt count")
    void recordAttempt_NewSolved() {
        RecordAttemptRequest request = RecordAttemptRequest.builder()
                .problemId("two-sum")
                .topic(Topic.ARRAYS)
                .difficulty(Difficulty.EASY)
                .status(ProgressStatus.SOLVED)
                .timeSpentSeconds(300L)
                .incrementAttempts(true)
                .build();

        when(userRepository.findById(sampleUserId)).thenReturn(Optional.of(sampleUser));
        when(progressTrackRepository.findByUserIdAndProblemId(sampleUserId, "two-sum"))
                .thenReturn(Optional.empty());

        when(progressTrackRepository.save(any(ProgressTrack.class)))
                .thenAnswer(inv -> {
                    ProgressTrack p = inv.getArgument(0);
                    p.setId(UUID.randomUUID());
                    return p;
                });

        ProgressTrackDto result = progressService.recordAttempt(sampleUserId, request);

        assertThat(result).isNotNull();
        assertThat(result.getProblemId()).isEqualTo("two-sum");
        assertThat(result.getStatus()).isEqualTo(ProgressStatus.SOLVED);
        assertThat(result.getAttempts()).isEqualTo(1);
        assertThat(result.getTimeSpentSeconds()).isEqualTo(300L);
        assertThat(result.getSolvedAt()).isNotNull();
    }

    @Test
    @DisplayName("getProgressSummary: Accurately computes solved metrics and streaks")
    void getProgressSummary_ComputesMetrics() {
        when(progressTrackRepository.countByUserIdAndStatus(sampleUserId, ProgressStatus.SOLVED))
                .thenReturn(3L);

        when(progressTrackRepository.countByTopicAndStatus(sampleUserId, ProgressStatus.SOLVED))
                .thenReturn(List.of(
                        new Object[]{Topic.ARRAYS, 2L},
                        new Object[]{Topic.TREES, 1L}
                ));

        when(progressTrackRepository.countTotalAttemptedByTopic(sampleUserId))
                .thenReturn(List.of(
                        new Object[]{Topic.ARRAYS, 3L},
                        new Object[]{Topic.TREES, 1L}
                ));

        when(progressTrackRepository.countByDifficultyAndStatus(sampleUserId, ProgressStatus.SOLVED))
                .thenReturn(List.of(
                        new Object[]{Difficulty.EASY, 2L},
                        new Object[]{Difficulty.MEDIUM, 1L}
                ));

        Instant today = Instant.now();
        Instant yesterday = today.minus(1, ChronoUnit.DAYS);

        ProgressTrack track1 = ProgressTrack.builder()
                .id(UUID.randomUUID())
                .problemId("p1")
                .topic(Topic.ARRAYS)
                .difficulty(Difficulty.EASY)
                .status(ProgressStatus.SOLVED)
                .solvedAt(yesterday)
                .lastAttemptedAt(yesterday)
                .build();

        ProgressTrack track2 = ProgressTrack.builder()
                .id(UUID.randomUUID())
                .problemId("p2")
                .topic(Topic.ARRAYS)
                .difficulty(Difficulty.EASY)
                .status(ProgressStatus.SOLVED)
                .solvedAt(today)
                .lastAttemptedAt(today)
                .build();

        when(progressTrackRepository.findSolvedProblemsOrderedBySolvedAt(sampleUserId))
                .thenReturn(List.of(track1, track2));

        when(progressTrackRepository.findRecentActivitySince(eq(sampleUserId), any(Instant.class)))
                .thenReturn(List.of(track1, track2));

        ProgressSummaryResponse summary = progressService.getProgressSummary(sampleUserId);

        assertThat(summary.getTotalSolved()).isEqualTo(3L);
        assertThat(summary.getSolvedByDifficulty().getEasySolved()).isEqualTo(2L);
        assertThat(summary.getSolvedByDifficulty().getMediumSolved()).isEqualTo(1L);
        assertThat(summary.getCurrentStreakDays()).isGreaterThanOrEqualTo(2);
        assertThat(summary.getLongestStreakDays()).isGreaterThanOrEqualTo(2);
        assertThat(summary.getSolvedByTopic()).hasSize(Topic.values().length);
        assertThat(summary.getLast30DaysActivity()).hasSize(30);
    }
}
