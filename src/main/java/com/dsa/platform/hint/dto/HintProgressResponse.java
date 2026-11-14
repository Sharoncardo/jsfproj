package com.dsa.platform.hint.dto;

import java.util.List;

public class HintProgressResponse {
    private Long problemId;
    private int currentUnlockedLevel; // 0 to 4
    private int maxLevel;             // usually 4
    private int totalHints;           // count of hints available
    private boolean hasMoreHints;     // true if currentUnlockedLevel < totalHints
    private Integer nextLevel;        // next level to unlock, null if max reached
    private List<HintDTO> unlockedHints; // ONLY the hints unlocked so far!
    private String statusMessage;

    public HintProgressResponse() {}

    public HintProgressResponse(Long problemId, int currentUnlockedLevel, int maxLevel, int totalHints,
                                boolean hasMoreHints, Integer nextLevel, List<HintDTO> unlockedHints,
                                String statusMessage) {
        this.problemId = problemId;
        this.currentUnlockedLevel = currentUnlockedLevel;
        this.maxLevel = maxLevel;
        this.totalHints = totalHints;
        this.hasMoreHints = hasMoreHints;
        this.nextLevel = nextLevel;
        this.unlockedHints = unlockedHints;
        this.statusMessage = statusMessage;
    }

    public Long getProblemId() {
        return problemId;
    }

    public void setProblemId(Long problemId) {
        this.problemId = problemId;
    }

    public int getCurrentUnlockedLevel() {
        return currentUnlockedLevel;
    }

    public void setCurrentUnlockedLevel(int currentUnlockedLevel) {
        this.currentUnlockedLevel = currentUnlockedLevel;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }

    public int getTotalHints() {
        return totalHints;
    }

    public void setTotalHints(int totalHints) {
        this.totalHints = totalHints;
    }

    public boolean isHasMoreHints() {
        return hasMoreHints;
    }

    public void setHasMoreHints(boolean hasMoreHints) {
        this.hasMoreHints = hasMoreHints;
    }

    public Integer getNextLevel() {
        return nextLevel;
    }

    public void setNextLevel(Integer nextLevel) {
        this.nextLevel = nextLevel;
    }

    public List<HintDTO> getUnlockedHints() {
        return unlockedHints;
    }

    public void setUnlockedHints(List<HintDTO> unlockedHints) {
        this.unlockedHints = unlockedHints;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }
}
