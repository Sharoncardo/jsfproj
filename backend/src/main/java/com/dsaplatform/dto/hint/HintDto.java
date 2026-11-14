package com.dsaplatform.dto.hint;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HintDto {
    private Long id;
    private int level;
    private String title;
    private String content; // Masked / null if locked
    private String timeComplexity;
    private String spaceComplexity;
    private boolean unlocked;
}
