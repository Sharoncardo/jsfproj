package com.dsa.platform.problem.dto;

import java.util.List;
import com.dsa.platform.topic.dto.TopicDTO;

public class ProblemDetailDTO {
    private Long id;
    private String title;
    private String slug;
    private String difficulty;
    private String description;
    private String inputFormat;
    private String outputFormat;
    private String constraints;
    private String sampleInput;
    private String sampleOutput;
    private String sampleExplanation;
    private TopicDTO topic;
    private List<LanguageOptionDTO> languageOptions;
    private int totalHints;

    public ProblemDetailDTO() {}

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInputFormat() {
        return inputFormat;
    }

    public void setInputFormat(String inputFormat) {
        this.inputFormat = inputFormat;
    }

    public String getOutputFormat() {
        return outputFormat;
    }

    public void setOutputFormat(String outputFormat) {
        this.outputFormat = outputFormat;
    }

    public String getConstraints() {
        return constraints;
    }

    public void setConstraints(String constraints) {
        this.constraints = constraints;
    }

    public String getSampleInput() {
        return sampleInput;
    }

    public void setSampleInput(String sampleInput) {
        this.sampleInput = sampleInput;
    }

    public String getSampleOutput() {
        return sampleOutput;
    }

    public void setSampleOutput(String sampleOutput) {
        this.sampleOutput = sampleOutput;
    }

    public String getSampleExplanation() {
        return sampleExplanation;
    }

    public void setSampleExplanation(String sampleExplanation) {
        this.sampleExplanation = sampleExplanation;
    }

    public TopicDTO getTopic() {
        return topic;
    }

    public void setTopic(TopicDTO topic) {
        this.topic = topic;
    }

    public List<LanguageOptionDTO> getLanguageOptions() {
        return languageOptions;
    }

    public void setLanguageOptions(List<LanguageOptionDTO> languageOptions) {
        this.languageOptions = languageOptions;
    }

    public int getTotalHints() {
        return totalHints;
    }

    public void setTotalHints(int totalHints) {
        this.totalHints = totalHints;
    }
}
