package com.dsaplatform.dto.progress;

import com.dsaplatform.entity.enums.Difficulty;
import com.dsaplatform.entity.enums.ProgressStatus;
import com.dsaplatform.entity.enums.Topic;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecordAttemptRequest {

    @NotBlank(message = "problemId is required")
    private String problemId;

    @NotNull(message = "topic is required")
    private Topic topic;

    @NotNull(message = "difficulty is required")
    private Difficulty difficulty;

    @NotNull(message = "status is required")
    private ProgressStatus status;

    @Min(value = 0, message = "timeSpentSeconds cannot be negative")
    @Builder.Default
    private long timeSpentSeconds = 0L;

    @Builder.Default
    private Boolean incrementAttempts = true;
}
