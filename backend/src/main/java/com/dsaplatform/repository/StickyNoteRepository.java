package com.dsaplatform.repository;

import com.dsaplatform.entity.StickyNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StickyNoteRepository extends JpaRepository<StickyNote, UUID> {

    List<StickyNote> findAllByUserIdOrderByCreatedAtDesc(UUID userId);

    Optional<StickyNote> findByIdAndUserId(UUID id, UUID userId);

    List<StickyNote> findAllByUserIdAndProblemId(UUID userId, String problemId);

    void deleteByIdAndUserId(UUID id, UUID userId);
}
