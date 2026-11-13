package com.dsaplatform.repository;

import com.dsaplatform.entity.ProgressTrack;
import com.dsaplatform.entity.enums.Difficulty;
import com.dsaplatform.entity.enums.ProgressStatus;
import com.dsaplatform.entity.enums.Topic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProgressTrackRepository extends JpaRepository<ProgressTrack, UUID> {

    Optional<ProgressTrack> findByUserIdAndProblemId(UUID userId, String problemId);

    List<ProgressTrack> findAllByUserId(UUID userId);

    Page<ProgressTrack> findAllByUserId(UUID userId, Pageable pageable);

    Page<ProgressTrack> findAllByUserIdAndTopic(UUID userId, Topic topic, Pageable pageable);

    Page<ProgressTrack> findAllByUserIdAndStatus(UUID userId, ProgressStatus status, Pageable pageable);

    long countByUserIdAndStatus(UUID userId, ProgressStatus status);

    @Query("SELECT p.topic, COUNT(p) FROM ProgressTrack p WHERE p.user.id = :userId AND p.status = :status GROUP BY p.topic")
    List<Object[]> countByTopicAndStatus(@Param("userId") UUID userId, @Param("status") ProgressStatus status);

    @Query("SELECT p.difficulty, COUNT(p) FROM ProgressTrack p WHERE p.user.id = :userId AND p.status = :status GROUP BY p.difficulty")
    List<Object[]> countByDifficultyAndStatus(@Param("userId") UUID userId, @Param("status") ProgressStatus status);

    @Query("SELECT p.topic, COUNT(p) FROM ProgressTrack p WHERE p.user.id = :userId GROUP BY p.topic")
    List<Object[]> countTotalAttemptedByTopic(@Param("userId") UUID userId);

    @Query("SELECT p FROM ProgressTrack p WHERE p.user.id = :userId AND p.status = 'SOLVED' AND p.solvedAt IS NOT NULL ORDER BY p.solvedAt ASC")
    List<ProgressTrack> findSolvedProblemsOrderedBySolvedAt(@Param("userId") UUID userId);

    @Query("SELECT p FROM ProgressTrack p WHERE p.user.id = :userId AND p.lastAttemptedAt >= :since ORDER BY p.lastAttemptedAt ASC")
    List<ProgressTrack> findRecentActivitySince(@Param("userId") UUID userId, @Param("since") Instant since);
}
