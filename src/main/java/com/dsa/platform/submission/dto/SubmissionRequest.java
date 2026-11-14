package com.dsa.platform.submission.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SubmissionRequest {

    @NotNull(message = "Problem ID is required")
    private Long problemId;

    @NotBlank(message = "Programming language is required")
    private String language; // cpp, java, python

    @NotBlank(message = "Source code cannot be empty")
    private String sourceCode;

    private String sessionId;

    public SubmissionRequest() {}

    public SubmissionRequest(Long problemId, String language, String sourceCode, String sessionId) {
        this.problemId = problemId;
        this.language = language;
        this.sourceCode = sourceCode;
        this.sessionId = sessionId;
    }

    public Long getProblemId() {
        return problemId;
    }

    public void setProblemId(Long problemId) {
        this.problemId = problemId;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
