package com.dsaplatform.repository;

import com.dsaplatform.entity.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long> {

    List<Submission> findByProblemIdOrderBySubmittedAtDesc(Long problemId);

    List<Submission> findByUserIdOrderBySubmittedAtDesc(UUID userId);

    List<Submission> findByProblemIdAndUserIdOrderBySubmittedAtDesc(Long problemId, UUID userId);
}
