package com.dsa.platform.hint.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import com.dsa.platform.hint.entity.Hint;

@Repository
public interface HintRepository extends JpaRepository<Hint, Long> {
    List<Hint> findByProblemIdOrderByLevelAsc(Long problemId);
    Optional<Hint> findByProblemIdAndLevel(Long problemId, int level);
    long countByProblemId(Long problemId);
}
