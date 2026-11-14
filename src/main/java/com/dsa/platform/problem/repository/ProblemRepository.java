package com.dsa.platform.problem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import com.dsa.platform.problem.entity.Problem;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, Long> {

    Optional<Problem> findBySlug(String slug);

    List<Problem> findByTopicId(Long topicId);

    List<Problem> findByTopicSlug(String topicSlug);

    List<Problem> findByDifficultyIgnoreCase(String difficulty);

    List<Problem> findByTitleContainingIgnoreCase(String title);

    @Query("SELECT p FROM Problem p WHERE " +
           "(:search IS NULL OR LOWER(p.title) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
           "(:topicSlug IS NULL OR :topicSlug = '' OR p.topic.slug = :topicSlug) AND " +
           "(:difficulty IS NULL OR :difficulty = '' OR LOWER(p.difficulty) = LOWER(:difficulty)) " +
           "ORDER BY p.id ASC")
    List<Problem> searchAndFilter(
            @Param("search") String search,
            @Param("topicSlug") String topicSlug,
            @Param("difficulty") String difficulty
    );
}
