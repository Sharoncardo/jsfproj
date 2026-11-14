package com.dsa.platform;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import com.dsa.platform.topic.dto.TopicDTO;
import com.dsa.platform.topic.entity.Topic;
import com.dsa.platform.topic.service.TopicService;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TopicServiceTest {

    @Autowired
    private TopicService topicService;

    @Test
    void testGetAllTopics() {
        List<TopicDTO> topics = topicService.getAllTopics();
        assertNotNull(topics);
        assertTrue(topics.size() >= 6, "Expected at least 6 default topics");

        for (int i = 0; i < topics.size() - 1; i++) {
            assertTrue(topics.get(i).getDisplayOrder() <= topics.get(i + 1).getDisplayOrder(),
                    "Topics should be sorted by displayOrder");
        }
    }

    @Test
    void testGetTopicBySlug() {
        TopicDTO topic = topicService.getTopicBySlug("arrays-and-hashing");
        assertNotNull(topic);
        assertEquals("Arrays & Hashing", topic.getName());
        assertEquals("arrays-and-hashing", topic.getSlug());
        assertNotNull(topic.getDescription());
    }

    @Test
    void testGetTopicById() {
        TopicDTO bySlug = topicService.getTopicBySlug("two-pointers");
        assertNotNull(bySlug);

        TopicDTO byId = topicService.getTopicById(bySlug.getId());
        assertNotNull(byId);
        assertEquals("Two Pointers", byId.getName());
        assertEquals(bySlug.getId(), byId.getId());
    }

    @Test
    void testGetTopicEntity() {
        TopicDTO dto = topicService.getTopicBySlug("stacks");
        Topic entity = topicService.getTopicEntity(dto.getId());

        assertNotNull(entity);
        assertEquals("Stacks", entity.getName());
        assertEquals("stacks", entity.getSlug());
    }

    @Test
    void testGetTopicByInvalidSlugThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            topicService.getTopicBySlug("non-existent-topic-slug");
        });
    }
}
