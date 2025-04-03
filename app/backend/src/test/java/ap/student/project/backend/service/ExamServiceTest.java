package ap.student.project.backend.service;

import ap.student.project.backend.dao.ExamRepository;
import ap.student.project.backend.dao.QuestionRepository;
import ap.student.project.backend.dto.ExamDTO;
import ap.student.project.backend.dto.QuestionDTO;
import ap.student.project.backend.entity.Exam;
import ap.student.project.backend.entity.Training;
import ap.student.project.backend.entity.Question;
import ap.student.project.backend.exceptions.ListFullException;
import ap.student.project.backend.exceptions.MissingArgumentException;
import ap.student.project.backend.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExamServiceTest {

    @Mock
    private ExamRepository examRepository;

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private TrainingService trainingService;

    @InjectMocks
    private ExamService examService;

    private ExamDTO examDTO;
    private Exam exam;
    private Training training;

    @BeforeEach
    void setUp() {
        examDTO = new ExamDTO(1, 1, 1, 1, 1); // Assuming trainingId is needed
        exam = new Exam();
        training = new Training();
    }

    @Test
    void save_ShouldThrowException_WhenTrainingIdIsZero() {
        ExamDTO invalidDTO = new ExamDTO(1, 1, 1, 1, 0);
        assertThrows(MissingArgumentException.class, () -> examService.save(invalidDTO));
    }

    @Test
    void save_ShouldThrowException_WhenTrainingNotFound() {
        when(trainingService.findById(1)).thenReturn(null);
        assertThrows(NotFoundException.class, () -> examService.save(examDTO));
    }

    @Test
    void save_ShouldSaveExam_WhenValidDTOIsProvided() {
        when(trainingService.findById(1)).thenReturn(training);

        examService.save(examDTO);

        verify(trainingService, times(1)).findById(1);
        verify(examRepository, times(1)).save(any(Exam.class));
    }

    @Test
    void findById_ShouldThrowException_WhenExamNotFound() {
        when(examRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> examService.findById(1));
    }

    @Test
    void findById_ShouldReturnExam_WhenFound() {
        when(examRepository.findById(1)).thenReturn(Optional.of(exam));
        Exam foundExam = examService.findById(1);
        assertNotNull(foundExam);
    }

    @Test
    void update_ShouldThrowException_WhenExamNotFound() {
        when(examRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> examService.update(1, examDTO));
    }

    @Test
    void update_ShouldSaveUpdatedExam_WhenFound() {
        when(examRepository.findById(1)).thenReturn(Optional.of(exam));

        examService.update(1, examDTO);

        verify(examRepository, times(1)).save(any(Exam.class));
    }

    @Test
    void delete_ShouldDeleteExam() {
        doNothing().when(examRepository).deleteById(1);
        examService.delete(1);
        verify(examRepository, times(1)).deleteById(1);
    }

    @Test
    void findAll_ShouldReturnListOfExams() {
        when(examRepository.findAll()).thenReturn(List.of(exam));
        List<Exam> result = examService.findAll();
        assertEquals(1, result.size());
        verify(examRepository, times(1)).findAll();
    }

    @Test
    void addQuestion_ShouldThrowException_WhenExamNotFound() {
        when(examRepository.findById(1)).thenReturn(Optional.empty());
        QuestionDTO questionDTO = new QuestionDTO(null, null, null);
        assertThrows(NotFoundException.class, () -> examService.addQuestion(1, questionDTO));
    }

    @Test
    void addQuestion_ShouldThrowException_WhenExamIsFull() {
        Exam mockExam = mock(Exam.class);
        when(examRepository.findById(1)).thenReturn(Optional.of(mockExam));
        when(mockExam.getQuestions()).thenReturn(List.of(new Question(), new Question()));
        when(mockExam.getQuestionAmount()).thenReturn(2);

        QuestionDTO questionDTO = new QuestionDTO(null, null, null);
        assertThrows(ListFullException.class, () -> examService.addQuestion(1, questionDTO));
    }

    @Test
    void findAllQuestionsByExamId_ShouldThrowException_WhenExamNotFound() {
        when(examRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> examService.findAllQuestionsByExamId(1));
    }
}
