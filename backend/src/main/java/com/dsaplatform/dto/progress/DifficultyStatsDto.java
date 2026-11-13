package com.dsaplatform.dto.progress;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DifficultyStatsDto {
    private long easySolved;
    private long mediumSolved;
    private long hardSolved;
    private long totalSolved;
}
