package com.dsaplatform.dto.submission;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubmissionResponse {
    private Long id;
    private Long problemId;
    private String problemTitle;
    private String problemSlug;
    private String language;
    private String status;
    private Integer executionTimeMs;
    private Integer memoryKb;
    private Instant submittedAt;
    private String feedback;
    private String sourceCode;
}
