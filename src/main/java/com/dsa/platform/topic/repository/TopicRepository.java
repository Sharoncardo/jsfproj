package com.dsa.platform.topic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import com.dsa.platform.topic.entity.Topic;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {
    Optional<Topic> findBySlug(String slug);
    Optional<Topic> findByName(String name);
    List<Topic> findAllByOrderByDisplayOrderAsc();
}
