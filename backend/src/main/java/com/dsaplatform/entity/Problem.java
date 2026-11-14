package com.dsaplatform.entity;

import com.dsaplatform.entity.enums.Difficulty;
import com.dsaplatform.entity.enums.Topic;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "problems")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Problem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "topic_id", nullable = false)
    private TopicEntity topic;

    @Enumerated(EnumType.STRING)
    @Column(name = "topic_code", length = 50, nullable = false)
    private Topic topicCode;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "slug", nullable = false, unique = true, length = 100)
    private String slug;

    @Enumerated(EnumType.STRING)
    @Column(name = "difficulty", nullable = false, length = 20)
    private Difficulty difficulty;

    @Column(name = "leetcode_number")
    private Integer leetcodeNumber;

    @Column(name = "description", columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(name = "input_format", columnDefinition = "TEXT")
    private String inputFormat;

    @Column(name = "output_format", columnDefinition = "TEXT")
    private String outputFormat;

    @Column(name = "constraints", columnDefinition = "TEXT")
    private String constraints;

    @Column(name = "sample_input", columnDefinition = "TEXT")
    private String sampleInput;

    @Column(name = "sample_output", columnDefinition = "TEXT")
    private String sampleOutput;

    @Column(name = "sample_explanation", columnDefinition = "TEXT")
    private String sampleExplanation;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @OneToMany(mappedBy = "problem", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("level ASC")
    @Builder.Default
    private List<Hint> hints = new ArrayList<>();

    @OneToMany(mappedBy = "problem", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<LanguageOption> languageOptions = new ArrayList<>();
}
