package com.dsaplatform.dto.progress;

import com.dsaplatform.entity.enums.Topic;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopicStatsDto {
    private Topic topic;
    private String displayName;
    private long solvedCount;
    private long attemptedCount;
    private int estimatedTotalInPlatform;
    private double completionPercentage;
}
