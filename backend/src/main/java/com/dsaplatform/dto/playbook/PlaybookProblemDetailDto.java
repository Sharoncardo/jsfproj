package com.dsaplatform.dto.playbook;

import com.dsaplatform.dto.hint.HintDto;
import com.dsaplatform.dto.revision.ProblemRevisionDto;
import com.dsaplatform.entity.enums.Difficulty;
import com.dsaplatform.entity.enums.ProgressStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlaybookProblemDetailDto {
    private Long problemId;
    private String title;
    private String slug;
    private Difficulty difficulty;
    private Integer leetcodeNumber;
    private String topicCode;
    private String topicName;
    private String description;
    private String inputFormat;
    private String outputFormat;
    private String constraints;
    private String sampleInput;
    private String sampleOutput;
    private String sampleExplanation;
    private ProgressStatus progressStatus;
    private int attempts;
    private long timeSpentSeconds;
    private Instant solvedAt;
    @Builder.Default
    private List<HintDto> hints = new ArrayList<>();
    private ProblemRevisionDto revision;
}
