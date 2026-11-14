package com.dsaplatform.dto.problem;

import com.dsaplatform.entity.enums.Difficulty;
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
public class ProblemDetailDto {
    private Long id;
    private String title;
    private String slug;
    private Difficulty difficulty;
    private Integer leetcodeNumber;
    private String topicCode;
    private String topicName;
    private String topicSlug;
    private String description;
    private String inputFormat;
    private String outputFormat;
    private String constraints;
    private String sampleInput;
    private String sampleOutput;
    private String sampleExplanation;
    @Builder.Default
    private List<LanguageOptionDto> languageOptions = new ArrayList<>();
    @Builder.Default
    private int totalHints = 4;
}
