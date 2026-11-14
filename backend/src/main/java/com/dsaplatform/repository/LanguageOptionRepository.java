package com.dsaplatform.repository;

import com.dsaplatform.entity.LanguageOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LanguageOptionRepository extends JpaRepository<LanguageOption, Long> {

    List<LanguageOption> findByProblemId(Long problemId);

    Optional<LanguageOption> findByProblemIdAndLanguageKey(Long problemId, String languageKey);
}
