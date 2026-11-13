package com.dsaplatform.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "bookmarks",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_bookmark_user_problem", columnNames = {"user_id", "problem_id"})
        },
        indexes = {
                @Index(name = "idx_bookmarks_user", columnList = "user_id"),
                @Index(name = "idx_bookmarks_problem", columnList = "problem_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bookmark {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "problem_id", length = 100, nullable = false)
    private String problemId;

    @Column(name = "insight", columnDefinition = "TEXT")
    private String insight;

    @Column(name = "tags", length = 255)
    private String tags;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
}
