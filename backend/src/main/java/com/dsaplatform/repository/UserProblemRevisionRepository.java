package com.dsaplatform.repository;

import com.dsaplatform.entity.UserProblemRevision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserProblemRevisionRepository extends JpaRepository<UserProblemRevision, UUID> {

    Optional<UserProblemRevision> findByUserIdAndProblemId(UUID userId, Long problemId);

    List<UserProblemRevision> findByUserId(UUID userId);
}
