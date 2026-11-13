package com.dsaplatform.entity;

import com.dsaplatform.entity.enums.Difficulty;
import com.dsaplatform.entity.enums.ProgressStatus;
import com.dsaplatform.entity.enums.Topic;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "progress_tracks",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_progress_user_problem", columnNames = {"user_id", "problem_id"})
        },
        indexes = {
                @Index(name = "idx_progress_user", columnList = "user_id"),
                @Index(name = "idx_progress_topic", columnList = "topic"),
                @Index(name = "idx_progress_status", columnList = "status"),
                @Index(name = "idx_progress_solved_at", columnList = "solved_at")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProgressTrack {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "problem_id", length = 100, nullable = false)
    private String problemId;

    @Enumerated(EnumType.STRING)
    @Column(name = "topic", length = 50, nullable = false)
    private Topic topic;

    @Enumerated(EnumType.STRING)
    @Column(name = "difficulty", length = 20, nullable = false)
    private Difficulty difficulty;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    @Builder.Default
    private ProgressStatus status = ProgressStatus.NOT_STARTED;

    @Column(name = "attempts", nullable = false)
    @Builder.Default
    private int attempts = 0;

    @Column(name = "time_spent_seconds", nullable = false)
    @Builder.Default
    private long timeSpentSeconds = 0L;

    @Column(name = "solved_at")
    private Instant solvedAt;

    @Column(name = "last_attempted_at", nullable = false)
    @Builder.Default
    private Instant lastAttemptedAt = Instant.now();
}
