package com.dsaplatform.repository;

import com.dsaplatform.entity.Hint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HintRepository extends JpaRepository<Hint, Long> {

    List<Hint> findByProblemIdOrderByLevelAsc(Long problemId);

    Optional<Hint> findByProblemIdAndLevel(Long problemId, int level);

    long countByProblemId(Long problemId);
}
