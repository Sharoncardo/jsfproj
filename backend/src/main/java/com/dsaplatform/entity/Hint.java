package com.dsaplatform.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "hints")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Hint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "problem_id", nullable = false)
    private Problem problem;

    @Column(name = "level", nullable = false)
    private int level;

    @Column(name = "title", nullable = false, length = 150)
    private String title;

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "time_complexity", length = 100)
    private String timeComplexity;

    @Column(name = "space_complexity", length = 100)
    private String spaceComplexity;
}
