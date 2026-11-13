package com.dsaplatform.entity;

import com.dsaplatform.entity.enums.NoteType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "sticky_notes",
        indexes = {
                @Index(name = "idx_notes_user", columnList = "user_id"),
                @Index(name = "idx_notes_problem", columnList = "problem_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StickyNote {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "problem_id", length = 100)
    private String problemId;

    @Column(name = "title", length = 120, nullable = false)
    private String title;

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = 20, nullable = false)
    @Builder.Default
    private NoteType type = NoteType.NOTE;

    @Column(name = "color", length = 20, nullable = false)
    @Builder.Default
    private String color = "#FEF08A";

    @Column(name = "position_x", nullable = false)
    @Builder.Default
    private double positionX = 100.0;

    @Column(name = "position_y", nullable = false)
    @Builder.Default
    private double positionY = 100.0;

    @Column(name = "width", nullable = false)
    @Builder.Default
    private double width = 280.0;

    @Column(name = "height", nullable = false)
    @Builder.Default
    private double height = 240.0;

    @Column(name = "pinned", nullable = false)
    @Builder.Default
    private boolean pinned = false;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;
}
