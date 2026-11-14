package com.dsa.platform.problem.dto;

public class ProblemSummaryDTO {
    private Long id;
    private String title;
    private String slug;
    private String difficulty;
    private String topicName;
    private String topicSlug;
    private int hintCount;

    public ProblemSummaryDTO() {}

    public ProblemSummaryDTO(Long id, String title, String slug, String difficulty, String topicName, String topicSlug, int hintCount) {
        this.id = id;
        this.title = title;
        this.slug = slug;
        this.difficulty = difficulty;
        this.topicName = topicName;
        this.topicSlug = topicSlug;
        this.hintCount = hintCount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getTopicSlug() {
        return topicSlug;
    }

    public void setTopicSlug(String topicSlug) {
        this.topicSlug = topicSlug;
    }

    public int getHintCount() {
        return hintCount;
    }

    public void setHintCount(int hintCount) {
        this.hintCount = hintCount;
    }
}
