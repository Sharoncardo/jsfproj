package com.dsaplatform.dto.progress;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProgressSummaryResponse {
    private long totalSolved;
    private long totalAttempted;
    private double overallCompletionPercentage;
    private int currentStreakDays;
    private int longestStreakDays;
    private List<TopicStatsDto> solvedByTopic;
    private DifficultyStatsDto solvedByDifficulty;
    private List<DailyActivityDto> last30DaysActivity;
}
