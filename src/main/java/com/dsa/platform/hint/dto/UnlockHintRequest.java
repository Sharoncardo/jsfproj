package com.dsa.platform.hint.dto;

public class UnlockHintRequest {
    private Integer currentLevel;
    private String sessionId;

    public UnlockHintRequest() {}

    public UnlockHintRequest(Integer currentLevel, String sessionId) {
        this.currentLevel = currentLevel;
        this.sessionId = sessionId;
    }

    public Integer getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(Integer currentLevel) {
        this.currentLevel = currentLevel;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
