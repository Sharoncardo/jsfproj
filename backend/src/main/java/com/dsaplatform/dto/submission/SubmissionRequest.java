package com.dsaplatform.dto.submission;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubmissionRequest {

    @NotNull(message = "problemId is required")
    private Long problemId;

    @NotBlank(message = "language is required")
    private String language;

    @NotBlank(message = "sourceCode is required")
    private String sourceCode;

    // Optional client session tag
    private String sessionId;
}
