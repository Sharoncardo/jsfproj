package com.dsaplatform.dto.hint;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HintProgressDto {
    private Long problemId;
    private int unlockedLevel;
    private int totalHints;
    private boolean hasMoreHints;
    private Integer nextLevel;
    @Builder.Default
    private List<HintDto> hints = new ArrayList<>();
    private String message;
}
