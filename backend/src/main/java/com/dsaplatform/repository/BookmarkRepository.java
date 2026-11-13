package com.dsaplatform.repository;

import com.dsaplatform.entity.Bookmark;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, UUID> {

    List<Bookmark> findAllByUserIdOrderByCreatedAtDesc(UUID userId);

    Page<Bookmark> findAllByUserIdOrderByCreatedAtDesc(UUID userId, Pageable pageable);

    Optional<Bookmark> findByIdAndUserId(UUID id, UUID userId);

    Optional<Bookmark> findByUserIdAndProblemId(UUID userId, String problemId);

    boolean existsByUserIdAndProblemId(UUID userId, String problemId);

    long countByUserId(UUID userId);

    void deleteByIdAndUserId(UUID id, UUID userId);
}
