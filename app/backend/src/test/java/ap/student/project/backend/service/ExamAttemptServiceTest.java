package ap.student.project.backend.service;

import ap.student.project.backend.dao.ExamAttemptRepository;
import ap.student.project.backend.dao.UserTrainingRepository;
import ap.student.project.backend.dto.ExamAttemptDTO;
import ap.student.project.backend.entity.ExamAttempt;
import ap.student.project.backend.entity.UserTraining;
import ap.student.project.backend.exceptions.MissingArgumentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ExamAttemptServiceTest {

    @Autowired
    private ExamAttemptService examAttemptService;

    @Autowired
    private ExamAttemptRepository examAttemptRepository;

    @Autowired
    private UserTrainingService userTrainingService;

    @Autowired
    private UserTrainingRepository userTrainingRepository;

    private UserTraining userTraining;
    private ExamAttemptDTO examAttemptDTO;

    @BeforeEach
    void setUp() {
        examAttemptRepository.deleteAll();
        userTrainingRepository.deleteAll();

        userTraining = new UserTraining();
        userTraining = userTrainingRepository.save(userTraining);

        examAttemptDTO = new ExamAttemptDTO(null, null, null, 0, userTraining.getId());
    }

    @Test
    void save_ShouldThrowException_WhenUserTrainingIdIsZero() {
        ExamAttemptDTO invalidDTO = new ExamAttemptDTO(null, null, null, 0, 0);

        assertThrows(MissingArgumentException.class, () -> examAttemptService.save(invalidDTO));
    }

    @Test
    void save_ShouldSaveExamAttempt_WhenValidDTOIsProvided() {
        ExamAttempt savedAttempt = examAttemptService.save(examAttemptDTO);

        assertNotNull(savedAttempt);
        assertNotNull(savedAttempt.getId());
        assertEquals(userTraining.getId(), savedAttempt.getUserTraining().getId());

        Optional<ExamAttempt> fromDb = examAttemptRepository.findById(savedAttempt.getId());
        assertTrue(fromDb.isPresent());
        assertEquals(userTraining.getId(), fromDb.get().getUserTraining().getId());
    }

    @Test
    void findAll_ShouldReturnListOfExamAttempts() {
        examAttemptService.save(examAttemptDTO);

        List<ExamAttempt> result = examAttemptService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(userTraining.getId(), result.get(0).getUserTraining().getId());
    }
}
