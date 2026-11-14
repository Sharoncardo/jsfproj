package com.dsaplatform.repository;

import com.dsaplatform.entity.Problem;
import com.dsaplatform.entity.enums.Difficulty;
import com.dsaplatform.entity.enums.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, Long> {

    Optional<Problem> findBySlug(String slug);

    List<Problem> findByTopicCode(Topic topicCode);

    List<Problem> findByDifficulty(Difficulty difficulty);

    @Query("SELECT p FROM Problem p WHERE " +
           "(:search IS NULL OR LOWER(p.title) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
           "(:topic IS NULL OR p.topicCode = :topic) AND " +
           "(:difficulty IS NULL OR p.difficulty = :difficulty) " +
           "ORDER BY p.id ASC")
    List<Problem> findWithFilters(@Param("search") String search,
                                  @Param("topic") Topic topic,
                                  @Param("difficulty") Difficulty difficulty);
}
