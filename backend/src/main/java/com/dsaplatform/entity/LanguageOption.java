package com.dsaplatform.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "language_options")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LanguageOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "problem_id", nullable = false)
    private Problem problem;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "language_key", nullable = false, length = 20)
    private String languageKey;

    @Column(name = "starter_template", columnDefinition = "TEXT", nullable = false)
    private String starterTemplate;
}
