package com.dsaplatform.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table(name = "submissions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "problem_id", nullable = false)
    private Problem problem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "language", nullable = false, length = 20)
    private String language;

    @Column(name = "source_code", columnDefinition = "TEXT", nullable = false)
    private String sourceCode;

    @Column(name = "status", nullable = false, length = 50)
    private String status;

    @Column(name = "execution_time_ms")
    private Integer executionTimeMs;

    @Column(name = "memory_kb")
    private Integer memoryKb;

    @CreationTimestamp
    @Column(name = "submitted_at", nullable = false, updatable = false)
    private Instant submittedAt;

    @Column(name = "feedback", columnDefinition = "TEXT")
    private String feedback;

    @Column(name = "session_id", length = 100)
    private String sessionId;
}
