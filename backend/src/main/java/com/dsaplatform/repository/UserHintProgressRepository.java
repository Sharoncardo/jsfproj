package com.dsaplatform.repository;

import com.dsaplatform.entity.UserHintProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserHintProgressRepository extends JpaRepository<UserHintProgress, UUID> {

    Optional<UserHintProgress> findByUserIdAndProblemId(UUID userId, Long problemId);
}
