package com.dsaplatform.dto.revision;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaveRevisionRequest {
    private String personalMnemonic;
    private String personalApproach;
    private String reasoning;
    private String timeComplexity;
    private String timeComplexityExplanation;
    private String spaceComplexity;
    private String spaceComplexityExplanation;
}
