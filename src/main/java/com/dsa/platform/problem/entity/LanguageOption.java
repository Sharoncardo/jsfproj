package com.dsa.platform.problem.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "language_options")
public class LanguageOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name; // C++, Java, Python

    @Column(nullable = false, length = 20)
    private String languageKey; // cpp, java, python

    @Column(columnDefinition = "TEXT")
    private String starterTemplate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id", nullable = false)
    @JsonIgnore
    private Problem problem;

    public LanguageOption() {}

    public LanguageOption(String name, String languageKey, String starterTemplate) {
        this.name = name;
        this.languageKey = languageKey;
        this.starterTemplate = starterTemplate;
    }

    public LanguageOption(String name, String languageKey, String starterTemplate, Problem problem) {
        this.name = name;
        this.languageKey = languageKey;
        this.starterTemplate = starterTemplate;
        this.problem = problem;
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

    public Problem getProblem() {
        return problem;
    }

    public void setProblem(Problem problem) {
        this.problem = problem;
    }
}
