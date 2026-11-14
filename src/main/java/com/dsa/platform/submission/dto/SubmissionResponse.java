package com.dsa.platform.submission.dto;

import java.time.LocalDateTime;

public class SubmissionResponse {
    private Long id;
    private Long problemId;
    private String problemTitle;
    private String language;
    private String status;
    private Integer executionTimeMs;
    private Integer memoryKb;
    private LocalDateTime submittedAt;
    private String feedback;
    private String sourceCode;

    public SubmissionResponse() {}

    public SubmissionResponse(Long id, Long problemId, String problemTitle, String language,
                              String status, Integer executionTimeMs, Integer memoryKb,
                              LocalDateTime submittedAt, String feedback, String sourceCode) {
        this.id = id;
        this.problemId = problemId;
        this.problemTitle = problemTitle;
        this.language = language;
        this.status = status;
        this.executionTimeMs = executionTimeMs;
        this.memoryKb = memoryKb;
        this.submittedAt = submittedAt;
        this.feedback = feedback;
        this.sourceCode = sourceCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProblemId() {
        return problemId;
    }

    public void setProblemId(Long problemId) {
        this.problemId = problemId;
    }

    public String getProblemTitle() {
        return problemTitle;
    }

    public void setProblemTitle(String problemTitle) {
        this.problemTitle = problemTitle;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getExecutionTimeMs() {
        return executionTimeMs;
    }

    public void setExecutionTimeMs(Integer executionTimeMs) {
        this.executionTimeMs = executionTimeMs;
    }

    public Integer getMemoryKb() {
        return memoryKb;
    }

    public void setMemoryKb(Integer memoryKb) {
        this.memoryKb = memoryKb;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }
}
