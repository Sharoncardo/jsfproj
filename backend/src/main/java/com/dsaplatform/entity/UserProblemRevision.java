package com.dsaplatform.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "user_problem_revisions",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_user_problem_rev", columnNames = {"user_id", "problem_id"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProblemRevision {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "problem_id", nullable = false)
    private Problem problem;

    @Column(name = "personal_mnemonic", columnDefinition = "TEXT")
    private String personalMnemonic;

    @Column(name = "personal_approach", columnDefinition = "TEXT")
    private String personalApproach;

    @Column(name = "reasoning", columnDefinition = "TEXT")
    private String reasoning;

    @Column(name = "time_complexity", length = 50)
    private String timeComplexity;

    @Column(name = "time_complexity_explanation", columnDefinition = "TEXT")
    private String timeComplexityExplanation;

    @Column(name = "space_complexity", length = 50)
    private String spaceComplexity;

    @Column(name = "space_complexity_explanation", columnDefinition = "TEXT")
    private String spaceComplexityExplanation;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
}
