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
public class PatternPlaybookDto {
    private String topicCode;
    private String topicName;
    private String topicSlug;
    private String description;
    private int problemCount;
    @Builder.Default
    private List<PlaybookProblemDetailDto> problems = new ArrayList<>();
}
