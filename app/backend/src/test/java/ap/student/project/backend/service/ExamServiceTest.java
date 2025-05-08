package ap.student.project.backend.service;

import ap.student.project.backend.dao.ExamRepository;
import ap.student.project.backend.dao.QuestionRepository;
import ap.student.project.backend.dao.TrainingRepository;
import ap.student.project.backend.dto.ExamDTO;
import ap.student.project.backend.dto.QuestionDTO;
import ap.student.project.backend.entity.Exam;
import ap.student.project.backend.entity.Language;
import ap.student.project.backend.entity.Question;
import ap.student.project.backend.entity.Training;
import ap.student.project.backend.exceptions.ListFullException;
import ap.student.project.backend.exceptions.MissingArgumentException;
import ap.student.project.backend.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ExamServiceTest {

    @Autowired
    private ExamService examService;

    @Autowired
    private TrainingService trainingService;

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private TrainingRepository trainingRepository;

    private Training testTraining;
    private Exam testExam;

    @BeforeEach
    void setUp() {
        questionRepository.deleteAll();
        examRepository.deleteAll();
        trainingRepository.deleteAll();

        Map<Language, String> title = new HashMap<>();
        title.put(Language.ENGLISH, "Test Training Title");
        title.put(Language.DUTCH, "Test Training Titel");

        Map<Language, String> description = new HashMap<>();
        description.put(Language.ENGLISH, "Test Description");
        description.put(Language.DUTCH, "Test Beschrijving");

        testTraining = new Training(title, description, true, null, null);
        testTraining = trainingRepository.save(testTraining);

        // Create and save an exam
        testExam = new Exam(70, 3, 60, 2, new ArrayList<Question>(), testTraining); // passingScore, maxAttempts, time, questionAmount, training
        testExam = examRepository.save(testExam);
    }

    @Test
    void save_ShouldThrowException_WhenTrainingIdIsZero() {
        ExamDTO invalidDTO = new ExamDTO(70, 3, 60, 2, 0);
        assertThrows(MissingArgumentException.class, () -> examService.save(invalidDTO));
    }

    @Test
    void save_ShouldThrowException_WhenTrainingNotFound() {
        ExamDTO invalidDTO = new ExamDTO(70, 3, 60, 2, 9999);
        assertThrows(NotFoundException.class, () -> examService.save(invalidDTO));
    }

    @Test
    void save_ShouldSaveExam_WhenValidDTOIsProvided() {
        ExamDTO examDTO = new ExamDTO(80, 2, 45, 5, testTraining.getId());
        examService.save(examDTO);

        List<Exam> exams = examService.findAll();
        assertEquals(2, exams.size()); // original + new
    }

    @Test
    void findById_ShouldReturnExam_WhenFound() {
        Exam foundExam = examService.findById(testExam.getId());

        assertNotNull(foundExam);
        assertEquals(testExam.getId(), foundExam.getId());
    }

    @Test
    void findById_ShouldThrowException_WhenExamNotFound() {
        assertThrows(NotFoundException.class, () -> examService.findById(9999));
    }

    @Test
    void update_ShouldSaveUpdatedExam_WhenFound() {
        ExamDTO updatedDTO = new ExamDTO(90, 5, 90, 10, testTraining.getId());
        examService.update(testExam.getId(), updatedDTO);

        Exam updatedExam = examService.findById(testExam.getId());
        assertEquals(90, updatedExam.getPassingScore());
        assertEquals(5, updatedExam.getMaxAttempts());
    }

    @Test
    void update_ShouldThrowException_WhenExamNotFound() {
        ExamDTO examDTO = new ExamDTO(80, 2, 45, 5, testTraining.getId());
        assertThrows(NotFoundException.class, () -> examService.update(9999, examDTO));
    }

    @Test
    void delete_ShouldDeleteExam() {
        examService.delete(testExam.getId());

        Optional<Exam> deletedExam = examRepository.findById(testExam.getId());
        assertFalse(deletedExam.isPresent());
    }

    @Test
    void findAll_ShouldReturnListOfExams() {
        List<Exam> exams = examService.findAll();
        assertEquals(1, exams.size());
    }

    @Test
    void addQuestion_ShouldAddQuestion_WhenExamIsNotFull() {
        QuestionDTO questionDTO = new QuestionDTO(null, null);

        examService.addQuestion(testExam.getId(), questionDTO);

        List<Question> questions = questionRepository.findAll();
        assertEquals(1, questions.size());
    }

    @Test
    void addQuestion_ShouldThrowException_WhenExamIsFull() {
        // Add maximum allowed questions
        for (int i = 0; i < testExam.getQuestionAmount(); i++) {
            QuestionDTO questionDTO = new QuestionDTO(null, null);
            examService.addQuestion(testExam.getId(), questionDTO);
        }

        testExam = examRepository.findById(testExam.getId()).orElseThrow();

        QuestionDTO extraQuestion = new QuestionDTO(null, null);

        assertThrows(ListFullException.class, () -> examService.addQuestion(testExam.getId(), extraQuestion));
    }

    @Test
    void findAllQuestionsByExamId_ShouldReturnQuestions() {
        QuestionDTO questionDTO = new QuestionDTO(null, null);
        examService.addQuestion(testExam.getId(), questionDTO);
        testExam = examRepository.findById(testExam.getId()).orElseThrow();
        List<Question> questions = examService.findAllQuestionsByExamId(testExam.getId());
        assertEquals(1, questions.size());
    }

    @Test
    void findAllQuestionsByExamId_ShouldThrowException_WhenExamNotFound() {
        assertThrows(NotFoundException.class, () -> examService.findAllQuestionsByExamId(9999));
    }
}
