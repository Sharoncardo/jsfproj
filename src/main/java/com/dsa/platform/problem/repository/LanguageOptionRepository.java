package com.dsa.platform.problem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import com.dsa.platform.problem.entity.LanguageOption;

@Repository
public interface LanguageOptionRepository extends JpaRepository<LanguageOption, Long> {
    List<LanguageOption> findByProblemId(Long problemId);
    Optional<LanguageOption> findByProblemIdAndLanguageKey(Long problemId, String languageKey);
}
