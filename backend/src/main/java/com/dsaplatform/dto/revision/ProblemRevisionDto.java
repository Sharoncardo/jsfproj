package com.dsaplatform.dto.revision;

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
public class ProblemRevisionDto {
    private UUID id;
    private Long problemId;
    private String problemTitle;
    private String problemSlug;
    private String personalMnemonic;
    private String personalApproach;
    private String reasoning;
    private String timeComplexity;
    private String timeComplexityExplanation;
    private String spaceComplexity;
    private String spaceComplexityExplanation;
    private Instant updatedAt;
}
