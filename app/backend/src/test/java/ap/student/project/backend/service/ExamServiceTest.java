package ap.student.project.backend.service;

import ap.student.project.backend.dao.ExamRepository;
import ap.student.project.backend.dao.QuestionRepository;
import ap.student.project.backend.dto.ExamDTO;
import ap.student.project.backend.dto.QuestionDTO;
import ap.student.project.backend.entity.Exam;
import ap.student.project.backend.entity.Question;
import ap.student.project.backend.entity.QuestionType;
import ap.student.project.backend.exceptions.ListFullException;
import ap.student.project.backend.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExamServiceTest {

    @Mock
    private ExamRepository examRepository;

    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private ExamService examService;

    private Exam exam;
    private ExamDTO examDTO;
    private QuestionDTO questionDTO;
    private Question question;

    @BeforeEach
    void setUp() {
        exam = new Exam();
        exam.setId(1);
        exam.setMaxAttempts(3);
        exam.setPassingScore(60);
        exam.setQuestionAmount(5);
        exam.setTime(60);
        exam.setQuestions(new ArrayList<>());

        examDTO = new ExamDTO(60, 3, 60, 5, 1);

        questionDTO = new QuestionDTO(null, QuestionType.MULTIPLE_CHOICE, null);

        question = new Question();
        question.setQuestionType(QuestionType.MULTIPLE_CHOICE);
    }

    @Test
    void testSave() {
        when(examRepository.save(any(Exam.class))).thenReturn(exam);
        examService.save(examDTO);
        verify(examRepository, times(1)).save(any(Exam.class));
    }

    @Test
    void testFindByIdFound() {
        when(examRepository.findById(1)).thenReturn(Optional.of(exam));
        Exam result = examService.findById(1);
        assertNotNull(result);
        assertEquals(1, result.getId());
    }

    @Test
    void testFindByIdNotFound() {
        when(examRepository.findById(1)).thenReturn(Optional.empty());
        NotFoundException exception = assertThrows(NotFoundException.class, () -> examService.findById(1));
        assertEquals("Exam with id 1 not found", exception.getMessage());
    }

    @Test
    void testUpdate() {
        when(examRepository.findById(1)).thenReturn(Optional.of(exam));
        when(examRepository.save(any(Exam.class))).thenReturn(exam);
        examService.update(1, examDTO);
        verify(examRepository, times(1)).save(any(Exam.class));
    }

    @Test
    void testUpdateNotFound() {
        when(examRepository.findById(1)).thenReturn(Optional.empty());
        NotFoundException exception = assertThrows(NotFoundException.class, () -> examService.update(1, examDTO));
        assertEquals("Exam with id 1 not found", exception.getMessage());
    }

    @Test
    void testFindAll() {
        when(examRepository.findAll()).thenReturn(Arrays.asList(exam));
        var result = examService.findAll();
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testDelete() {
        doNothing().when(examRepository).deleteById(1);
        examService.delete(1);
        verify(examRepository, times(1)).deleteById(1);
    }

    @Test
    void testAddQuestion() {
        when(examRepository.findById(1)).thenReturn(Optional.of(exam));
        when(questionRepository.save(any(Question.class))).thenReturn(question);
        examService.addQuestion(1, questionDTO);
        verify(questionRepository, times(1)).save(any(Question.class));
    }

    @Test
    void testAddQuestionExceedsLimit() {
        exam.setQuestions(Arrays.asList(new Question(), new Question(), new Question(), new Question(), new Question()));
        when(examRepository.findById(1)).thenReturn(Optional.of(exam));
        ListFullException exception = assertThrows(ListFullException.class, () -> examService.addQuestion(1, questionDTO));
        assertEquals("Exam with id 1 has a question limit of 5", exception.getMessage());
    }

    @Test
    void testFindAllQuestionsByExamId() {
        exam.setQuestions(Arrays.asList(question));
        when(examRepository.findById(1)).thenReturn(Optional.of(exam));
        var result = examService.findAllQuestionsByExamId(1);
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testFindAllQuestionsByExamIdNotFound() {
        when(examRepository.findById(1)).thenReturn(Optional.empty());
        NotFoundException exception = assertThrows(NotFoundException.class, () -> examService.findAllQuestionsByExamId(1));
        assertEquals("Exam with id 1 not found", exception.getMessage());
    }
}