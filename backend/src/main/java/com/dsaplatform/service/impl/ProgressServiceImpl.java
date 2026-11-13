package com.dsaplatform.service.impl;

import com.dsaplatform.dto.common.PageResponse;
import com.dsaplatform.dto.progress.*;
import com.dsaplatform.entity.ProgressTrack;
import com.dsaplatform.entity.User;
import com.dsaplatform.entity.enums.Difficulty;
import com.dsaplatform.entity.enums.ProgressStatus;
import com.dsaplatform.entity.enums.Topic;
import com.dsaplatform.exception.ResourceNotFoundException;
import com.dsaplatform.repository.ProgressTrackRepository;
import com.dsaplatform.repository.UserRepository;
import com.dsaplatform.service.ProgressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProgressServiceImpl implements ProgressService {

    private static final int DEFAULT_TOPIC_GOAL = 20;

    private final ProgressTrackRepository progressTrackRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ProgressTrackDto recordAttempt(UUID userId, RecordAttemptRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Optional<ProgressTrack> existingOpt = progressTrackRepository
                .findByUserIdAndProblemId(userId, request.getProblemId());

        ProgressTrack track;
        Instant now = Instant.now();

        if (existingOpt.isPresent()) {
            track = existingOpt.get();
            track.setTopic(request.getTopic());
            track.setDifficulty(request.getDifficulty());

            if (request.getStatus() == ProgressStatus.SOLVED) {
                if (track.getSolvedAt() == null) {
                    track.setSolvedAt(now);
                }
                track.setStatus(ProgressStatus.SOLVED);
            } else if (track.getStatus() != ProgressStatus.SOLVED) {
                track.setStatus(request.getStatus());
            }

            if (Boolean.TRUE.equals(request.getIncrementAttempts())) {
                track.setAttempts(track.getAttempts() + 1);
            }
            track.setTimeSpentSeconds(track.getTimeSpentSeconds() + request.getTimeSpentSeconds());
            track.setLastAttemptedAt(now);
        } else {
            track = ProgressTrack.builder()
                    .user(user)
                    .problemId(request.getProblemId())
                    .topic(request.getTopic())
                    .difficulty(request.getDifficulty())
                    .status(request.getStatus())
                    .attempts(Boolean.TRUE.equals(request.getIncrementAttempts()) ? 1 : 0)
                    .timeSpentSeconds(request.getTimeSpentSeconds())
                    .solvedAt(request.getStatus() == ProgressStatus.SOLVED ? now : null)
                    .lastAttemptedAt(now)
                    .build();
        }

        ProgressTrack saved = progressTrackRepository.save(track);
        return ProgressTrackDto.fromEntity(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ProgressSummaryResponse getProgressSummary(UUID userId) {
        long totalSolved = progressTrackRepository.countByUserIdAndStatus(userId, ProgressStatus.SOLVED);

        // Solved count per topic
        List<Object[]> solvedTopicRows = progressTrackRepository.countByTopicAndStatus(userId, ProgressStatus.SOLVED);
        Map<Topic, Long> solvedByTopicMap = new EnumMap<>(Topic.class);
        for (Object[] row : solvedTopicRows) {
            Topic topic = (Topic) row[0];
            Long count = (Long) row[1];
            solvedByTopicMap.put(topic, count);
        }

        // Attempted count per topic
        List<Object[]> attemptedTopicRows = progressTrackRepository.countTotalAttemptedByTopic(userId);
        Map<Topic, Long> attemptedByTopicMap = new EnumMap<>(Topic.class);
        long totalAttempted = 0L;
        for (Object[] row : attemptedTopicRows) {
            Topic topic = (Topic) row[0];
            Long count = (Long) row[1];
            attemptedByTopicMap.put(topic, count);
            totalAttempted += count;
        }

        List<TopicStatsDto> topicStatsList = new ArrayList<>();
        for (Topic topic : Topic.values()) {
            long solvedCount = solvedByTopicMap.getOrDefault(topic, 0L);
            long attemptedCount = attemptedByTopicMap.getOrDefault(topic, 0L);
            double pct = Math.min(100.0, Math.round(((double) solvedCount / DEFAULT_TOPIC_GOAL * 100.0) * 10.0) / 10.0);

            topicStatsList.add(TopicStatsDto.builder()
                    .topic(topic)
                    .displayName(topic.getDisplayName())
                    .solvedCount(solvedCount)
                    .attemptedCount(attemptedCount)
                    .estimatedTotalInPlatform(DEFAULT_TOPIC_GOAL)
                    .completionPercentage(pct)
                    .build());
        }

        // Difficulty stats
        List<Object[]> diffRows = progressTrackRepository.countByDifficultyAndStatus(userId, ProgressStatus.SOLVED);
        long easySolved = 0L;
        long mediumSolved = 0L;
        long hardSolved = 0L;
        for (Object[] row : diffRows) {
            Difficulty diff = (Difficulty) row[0];
            Long count = (Long) row[1];
            if (diff == Difficulty.EASY) easySolved = count;
            else if (diff == Difficulty.MEDIUM) mediumSolved = count;
            else if (diff == Difficulty.HARD) hardSolved = count;
        }

        DifficultyStatsDto diffStats = DifficultyStatsDto.builder()
                .easySolved(easySolved)
                .mediumSolved(mediumSolved)
                .hardSolved(hardSolved)
                .totalSolved(totalSolved)
                .build();

        // Calculate streaks
        List<ProgressTrack> solvedTracks = progressTrackRepository.findSolvedProblemsOrderedBySolvedAt(userId);
        int[] streaks = calculateStreaks(solvedTracks);
        int currentStreak = streaks[0];
        int longestStreak = streaks[1];

        // Overall completion percentage
        double totalPlatformProblems = (double) DEFAULT_TOPIC_GOAL * Topic.values().length;
        double overallPct = Math.min(100.0, Math.round(((double) totalSolved / totalPlatformProblems * 100.0) * 10.0) / 10.0);

        // Last 30 days activity series
        List<DailyActivityDto> activityList = calculateLast30DaysActivity(userId);

        return ProgressSummaryResponse.builder()
                .totalSolved(totalSolved)
                .totalAttempted(totalAttempted)
                .overallCompletionPercentage(overallPct)
                .currentStreakDays(currentStreak)
                .longestStreakDays(longestStreak)
                .solvedByTopic(topicStatsList)
                .solvedByDifficulty(diffStats)
                .last30DaysActivity(activityList)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ProgressTrackDto> getProgressList(UUID userId, Topic topic, ProgressStatus status, Pageable pageable) {
        Page<ProgressTrack> page;
        if (topic != null) {
            page = progressTrackRepository.findAllByUserIdAndTopic(userId, topic, pageable);
        } else if (status != null) {
            page = progressTrackRepository.findAllByUserIdAndStatus(userId, status, pageable);
        } else {
            page = progressTrackRepository.findAllByUserId(userId, pageable);
        }

        return PageResponse.of(page.map(ProgressTrackDto::fromEntity));
    }

    private int[] calculateStreaks(List<ProgressTrack> solvedTracks) {
        if (solvedTracks.isEmpty()) {
            return new int[]{0, 0};
        }

        TreeSet<LocalDate> activeDates = new TreeSet<>();
        for (ProgressTrack track : solvedTracks) {
            if (track.getSolvedAt() != null) {
                activeDates.add(LocalDate.ofInstant(track.getSolvedAt(), ZoneOffset.UTC));
            }
        }

        if (activeDates.isEmpty()) {
            return new int[]{0, 0};
        }

        List<LocalDate> sortedDates = new ArrayList<>(activeDates);

        // Longest streak
        int longestStreak = 1;
        int currentConsecutive = 1;

        for (int i = 1; i < sortedDates.size(); i++) {
            if (sortedDates.get(i).equals(sortedDates.get(i - 1).plusDays(1))) {
                currentConsecutive++;
                longestStreak = Math.max(longestStreak, currentConsecutive);
            } else {
                currentConsecutive = 1;
            }
        }

        // Current streak
        LocalDate today = LocalDate.now(ZoneOffset.UTC);
        LocalDate yesterday = today.minusDays(1);

        int currentStreak = 0;
        LocalDate cursor;

        if (activeDates.contains(today)) {
            cursor = today;
        } else if (activeDates.contains(yesterday)) {
            cursor = yesterday;
        } else {
            return new int[]{0, longestStreak};
        }

        while (activeDates.contains(cursor)) {
            currentStreak++;
            cursor = cursor.minusDays(1);
        }

        return new int[]{currentStreak, Math.max(longestStreak, currentStreak)};
    }

    private List<DailyActivityDto> calculateLast30DaysActivity(UUID userId) {
        LocalDate today = LocalDate.now(ZoneOffset.UTC);
        LocalDate thirtyDaysAgo = today.minusDays(29);
        Instant sinceInstant = thirtyDaysAgo.atStartOfDay().toInstant(ZoneOffset.UTC);

        List<ProgressTrack> recentActivity = progressTrackRepository.findRecentActivitySince(userId, sinceInstant);

        Map<LocalDate, Long> dayCounts = new HashMap<>();
        for (ProgressTrack track : recentActivity) {
            Instant eventTime = track.getSolvedAt() != null ? track.getSolvedAt() : track.getLastAttemptedAt();
            LocalDate date = LocalDate.ofInstant(eventTime, ZoneOffset.UTC);
            if (!date.isBefore(thirtyDaysAgo) && !date.isAfter(today)) {
                dayCounts.put(date, dayCounts.getOrDefault(date, 0L) + 1);
            }
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        List<DailyActivityDto> activity = new ArrayList<>(30);

        for (int i = 0; i < 30; i++) {
            LocalDate d = thirtyDaysAgo.plusDays(i);
            activity.add(DailyActivityDto.builder()
                    .date(d.format(formatter))
                    .count(dayCounts.getOrDefault(d, 0L))
                    .build());
        }

        return activity;
    }
}
