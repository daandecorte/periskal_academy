package ap.student.project.backend.service;

import ap.student.project.backend.dao.TopicRepository;
import ap.student.project.backend.dto.TopicDTO;
import ap.student.project.backend.entity.Language;
import ap.student.project.backend.entity.Topic;
import ap.student.project.backend.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class TopicServiceTest {

    @Autowired
    private TopicService topicService;

    @Autowired
    private TopicRepository topicRepository;

    private Topic testTopic;
    private Map<Language, String> titleMap;

    @BeforeEach
    void setUp() {
        topicRepository.deleteAll();

        titleMap = new HashMap<>();
        titleMap.put(Language.ENGLISH, "Test Topic");
        titleMap.put(Language.DUTCH, "Test Onderwerp");

        testTopic = new Topic(titleMap);
        testTopic = topicRepository.save(testTopic);
    }

    @Test
    void findAll_shouldReturnAllTopics() {
        Map<Language, String> anotherTitleMap = new HashMap<>();
        anotherTitleMap.put(Language.ENGLISH, "Another Topic");
        anotherTitleMap.put(Language.DUTCH, "Nog een Onderwerp");
        Topic anotherTopic = new Topic(anotherTitleMap);
        topicRepository.save(anotherTopic);

        List<Topic> topics = topicService.findAll();

        assertNotNull(topics);
        assertEquals(2, topics.size());
        assertTrue(topics.stream().anyMatch(t -> t.getId() == testTopic.getId()));
    }

    @Test
    void findById_withValidId_shouldReturnTopic() {
        Topic foundTopic = topicService.findById(testTopic.getId());

        assertNotNull(foundTopic);
        assertEquals(testTopic.getId(), foundTopic.getId());
        assertEquals(testTopic.getTitle(), foundTopic.getTitle());
    }

    @Test
    void findById_withInvalidId_shouldThrowNotFoundException() {
        assertThrows(NotFoundException.class, () -> topicService.findById(999));
    }

    @Test
    void save_shouldCreateNewTopic() {
        Map<Language, String> newTitleMap = new HashMap<>();
        newTitleMap.put(Language.ENGLISH, "New Topic");
        newTitleMap.put(Language.DUTCH, "Nieuw Onderwerp");
        TopicDTO topicDTO = new TopicDTO(newTitleMap);

        Topic savedTopic = topicService.save(topicDTO);

        assertNotNull(savedTopic);
        assertNotNull(savedTopic.getId());
        assertEquals(newTitleMap, savedTopic.getTitle());

        Optional<Topic> fromDb = topicRepository.findById(savedTopic.getId());
        assertTrue(fromDb.isPresent());
        assertEquals(newTitleMap, fromDb.get().getTitle());
    }

    @Test
    void update_withValidId_shouldUpdateTopic() {
        Map<Language, String> updatedTitleMap = new HashMap<>();
        updatedTitleMap.put(Language.ENGLISH, "Updated Topic");
        updatedTitleMap.put(Language.DUTCH, "Bijgewerkt Onderwerp");
        TopicDTO topicDTO = new TopicDTO(updatedTitleMap);

        Topic updatedTopic = topicService.update(testTopic.getId(), topicDTO);

        assertNotNull(updatedTopic);
        assertEquals(testTopic.getId(), updatedTopic.getId());
        assertEquals(updatedTitleMap, updatedTopic.getTitle());

        Optional<Topic> fromDb = topicRepository.findById(testTopic.getId());
        assertTrue(fromDb.isPresent());
        assertEquals(updatedTitleMap, fromDb.get().getTitle());
    }

    @Test
    void update_withInvalidId_shouldThrowNotFoundException() {
        Map<Language, String> updatedTitleMap = new HashMap<>();
        updatedTitleMap.put(Language.ENGLISH, "Updated Topic");
        TopicDTO topicDTO = new TopicDTO(updatedTitleMap);

        assertThrows(NotFoundException.class, () -> topicService.update(999, topicDTO));
    }

    @Test
    void delete_withValidId_shouldDeleteTopic() {
        topicService.delete(testTopic.getId());

        Optional<Topic> fromDb = topicRepository.findById(testTopic.getId());
        assertFalse(fromDb.isPresent());
    }

    @Test
    void delete_withInvalidId_shouldThrowNotFoundException() {
        assertThrows(NotFoundException.class, () -> topicService.delete(999));
    }
}