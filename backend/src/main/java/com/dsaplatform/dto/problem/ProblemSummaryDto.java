package com.dsaplatform.dto.problem;

import com.dsaplatform.entity.enums.Difficulty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProblemSummaryDto {
    private Long id;
    private String title;
    private String slug;
    private Difficulty difficulty;
    private Integer leetcodeNumber;
    private String topicCode;
    private String topicName;
    private String topicSlug;
    private String descriptionSnippet;
}
