package com.dsaplatform.dto.progress;

import com.dsaplatform.entity.ProgressTrack;
import com.dsaplatform.entity.enums.Difficulty;
import com.dsaplatform.entity.enums.ProgressStatus;
import com.dsaplatform.entity.enums.Topic;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProgressTrackDto {
    private UUID id;
    private String problemId;
    private Topic topic;
    private String topicDisplayName;
    private Difficulty difficulty;
    private ProgressStatus status;
    private int attempts;
    private long timeSpentSeconds;
    private Instant solvedAt;
    private Instant lastAttemptedAt;

    public static ProgressTrackDto fromEntity(ProgressTrack entity) {
        return ProgressTrackDto.builder()
                .id(entity.getId())
                .problemId(entity.getProblemId())
                .topic(entity.getTopic())
                .topicDisplayName(entity.getTopic().getDisplayName())
                .difficulty(entity.getDifficulty())
                .status(entity.getStatus())
                .attempts(entity.getAttempts())
                .timeSpentSeconds(entity.getTimeSpentSeconds())
                .solvedAt(entity.getSolvedAt())
                .lastAttemptedAt(entity.getLastAttemptedAt())
                .build();
    }
}
