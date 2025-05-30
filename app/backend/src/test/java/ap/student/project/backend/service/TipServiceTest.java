package ap.student.project.backend.service;

import ap.student.project.backend.dao.TipRepository;
import ap.student.project.backend.dao.TopicRepository;
import ap.student.project.backend.dto.TipDTO;
import ap.student.project.backend.entity.Language;
import ap.student.project.backend.entity.Tip;
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
class TipServiceTest {

    @Autowired
    private TipService tipService;

    @Autowired
    private TipRepository tipRepository;

    @Autowired
    private TopicRepository topicRepository;

    private Tip testTip;
    private Topic testTopic;
    private Map<Language, String> textMap;
    private Map<Language, String> titleMap;

    @BeforeEach
    void setUp() {
        tipRepository.deleteAll();
        topicRepository.deleteAll();

        titleMap = new HashMap<>();
        titleMap.put(Language.ENGLISH, "Test Topic");
        titleMap.put(Language.DUTCH, "Test Onderwerp");
        testTopic = new Topic(titleMap);
        testTopic = topicRepository.save(testTopic);

        textMap = new HashMap<>();
        textMap.put(Language.ENGLISH, "This is a test tip");
        textMap.put(Language.DUTCH, "Dit is een test tip");
        testTip = new Tip(testTopic, textMap);
        testTip = tipRepository.save(testTip);
    }

    @Test
    void findAll_shouldReturnAllTips() {
        Map<Language, String> anotherTextMap = new HashMap<>();
        anotherTextMap.put(Language.ENGLISH, "Another tip");
        anotherTextMap.put(Language.DUTCH, "Nog een tip");
        Tip anotherTip = new Tip(testTopic, anotherTextMap);
        tipRepository.save(anotherTip);

        List<Tip> tips = tipService.findAll();

        assertNotNull(tips);
        assertEquals(2, tips.size());
        assertTrue(tips.stream().anyMatch(t -> t.getId() == testTip.getId()));
        assertTrue(tips.stream().anyMatch(t -> t.getId() == anotherTip.getId()));
    }

    @Test
    void findById_withValidId_shouldReturnTip() {
        Tip foundTip = tipService.findById(testTip.getId());

        assertNotNull(foundTip);
        assertEquals(testTip.getId(), foundTip.getId());
        assertEquals(testTip.getText(), foundTip.getText());
        assertEquals(testTip.getTopic().getId(), foundTip.getTopic().getId());
    }

    @Test
    void findById_withInvalidId_shouldThrowNotFoundException() {
        assertThrows(NotFoundException.class, () -> tipService.findById(999));
    }

    @Test
    void save_withValidTopicId_shouldCreateNewTip() {
        Map<Language, String> newTextMap = new HashMap<>();
        newTextMap.put(Language.ENGLISH, "New Tip");
        newTextMap.put(Language.DUTCH, "Nieuwe Tip");
        TipDTO tipDTO = new TipDTO(testTopic.getId(), newTextMap);

        Tip savedTip = tipService.save(tipDTO);

        assertNotNull(savedTip);
        assertNotNull(savedTip.getId());
        assertEquals(newTextMap, savedTip.getText());
        assertEquals(testTopic.getId(), savedTip.getTopic().getId());

        Optional<Tip> fromDb = tipRepository.findById(savedTip.getId());
        assertTrue(fromDb.isPresent());
        assertEquals(newTextMap, fromDb.get().getText());
        assertEquals(testTopic.getId(), fromDb.get().getTopic().getId());
    }

    @Test
    void save_withInvalidTopicId_shouldThrowNotFoundException() {
        Map<Language, String> newTextMap = new HashMap<>();
        newTextMap.put(Language.ENGLISH, "New Tip");
        TipDTO tipDTO = new TipDTO(999, newTextMap);

        assertThrows(NotFoundException.class, () -> tipService.save(tipDTO));
    }

    @Test
    void save_withNegativeTopicId_shouldThrowNotFoundException() {
        Map<Language, String> newTextMap = new HashMap<>();
        newTextMap.put(Language.ENGLISH, "New Tip");
        TipDTO tipDTO = new TipDTO(-1, newTextMap);

        assertThrows(NotFoundException.class, () -> tipService.save(tipDTO));
    }

    @Test
    void update_withValidId_shouldUpdateTip() {
        Map<Language, String> updatedTextMap = new HashMap<>();
        updatedTextMap.put(Language.ENGLISH, "Updated Tip");
        updatedTextMap.put(Language.DUTCH, "Bijgewerkte Tip");
        TipDTO tipDTO = new TipDTO(testTopic.getId(), updatedTextMap);

        tipService.update(testTip.getId(), tipDTO);

        Optional<Tip> fromDb = tipRepository.findById(testTip.getId());
        assertTrue(fromDb.isPresent());
        assertEquals(updatedTextMap, fromDb.get().getText());
        assertEquals(testTopic.getId(), fromDb.get().getTopic().getId());
    }

    @Test
    void update_withInvalidId_shouldThrowNotFoundException() {
        // Prepare test data
        Map<Language, String> updatedTextMap = new HashMap<>();
        updatedTextMap.put(Language.ENGLISH, "Updated Tip");
        TipDTO tipDTO = new TipDTO(testTopic.getId(), updatedTextMap);

        // Test & Assert
        assertThrows(NotFoundException.class, () -> tipService.update(999, tipDTO));
    }

    @Test
    void deleteById_withValidId_shouldDeleteTip() {
        // Test
        tipService.deleteById(testTip.getId());

        // Assertions
        Optional<Tip> fromDb = tipRepository.findById(testTip.getId());
        assertFalse(fromDb.isPresent());
    }

    @Test
    void deleteById_withInvalidId_shouldThrowNotFoundException() {
        // Test & Assert
        assertThrows(NotFoundException.class, () -> tipService.deleteById(999));
    }
}