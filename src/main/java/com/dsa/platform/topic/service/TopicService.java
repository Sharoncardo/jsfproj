package com.dsa.platform.topic.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import com.dsa.platform.topic.entity.Topic;
import com.dsa.platform.topic.repository.TopicRepository;
import com.dsa.platform.topic.dto.TopicDTO;

@Service
@Transactional(readOnly = true)
public class TopicService {

    private final TopicRepository topicRepository;

    public TopicService(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    public List<TopicDTO> getAllTopics() {
        return topicRepository.findAllByOrderByDisplayOrderAsc().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public TopicDTO getTopicById(Long id) {
        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Topic not found with id: " + id));
        return toDTO(topic);
    }

    public TopicDTO getTopicBySlug(String slug) {
        Topic topic = topicRepository.findBySlug(slug)
                .orElseThrow(() -> new IllegalArgumentException("Topic not found with slug: " + slug));
        return toDTO(topic);
    }

    public Topic getTopicEntity(Long id) {
        return topicRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Topic not found with id: " + id));
    }

    public TopicDTO toDTO(Topic topic) {
        int count = topic.getProblems() != null ? topic.getProblems().size() : 0;
        return new TopicDTO(
                topic.getId(),
                topic.getName(),
                topic.getSlug(),
                topic.getDescription(),
                topic.getDisplayOrder(),
                count
        );
    }
}
