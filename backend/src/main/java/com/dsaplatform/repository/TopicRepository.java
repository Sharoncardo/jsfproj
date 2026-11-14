package com.dsaplatform.repository;

import com.dsaplatform.entity.TopicEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TopicRepository extends JpaRepository<TopicEntity, Long> {
    Optional<TopicEntity> findByCode(String code);
    Optional<TopicEntity> findBySlug(String slug);
    List<TopicEntity> findAllByOrderByDisplayOrderAsc();
}
