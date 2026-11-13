package com.dsaplatform.dto.progress;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyActivityDto {
    private String date; // YYYY-MM-DD
    private long count;
}
