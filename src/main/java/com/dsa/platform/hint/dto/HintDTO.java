package com.dsa.platform.hint.dto;

public class HintDTO {
    private Long id;
    private int level;
    private String title;
    private String content;
    private String timeComplexity;
    private String spaceComplexity;

    public HintDTO() {}

    public HintDTO(Long id, int level, String title, String content, String timeComplexity, String spaceComplexity) {
        this.id = id;
        this.level = level;
        this.title = title;
        this.content = content;
        this.timeComplexity = timeComplexity;
        this.spaceComplexity = spaceComplexity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimeComplexity() {
        return timeComplexity;
    }

    public void setTimeComplexity(String timeComplexity) {
        this.timeComplexity = timeComplexity;
    }

    public String getSpaceComplexity() {
        return spaceComplexity;
    }

    public void setSpaceComplexity(String spaceComplexity) {
        this.spaceComplexity = spaceComplexity;
    }
}
