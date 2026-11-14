package com.dsaplatform.dto.playbook;

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
public class PlaybookResponse {
    @Builder.Default
    private List<PatternPlaybookDto> patterns = new ArrayList<>();
    private int totalAttemptedOrSolvedProblems;
}
