package com.dsa.platform.problem.dto;

public class LanguageOptionDTO {
    private Long id;
    private String name;
    private String languageKey;
    private String starterTemplate;

    public LanguageOptionDTO() {}

    public LanguageOptionDTO(Long id, String name, String languageKey, String starterTemplate) {
        this.id = id;
        this.name = name;
        this.languageKey = languageKey;
        this.starterTemplate = starterTemplate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLanguageKey() {
        return languageKey;
    }

    public void setLanguageKey(String languageKey) {
        this.languageKey = languageKey;
    }

    public String getStarterTemplate() {
        return starterTemplate;
    }

    public void setStarterTemplate(String starterTemplate) {
        this.starterTemplate = starterTemplate;
    }
}
