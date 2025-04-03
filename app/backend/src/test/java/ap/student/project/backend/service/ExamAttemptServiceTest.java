package ap.student.project.backend.service;

import ap.student.project.backend.dao.ExamAttemptRepository;
import ap.student.project.backend.dto.ExamAttemptDTO;
import ap.student.project.backend.entity.ExamAttempt;
import ap.student.project.backend.entity.UserTraining;
import ap.student.project.backend.exceptions.MissingArgumentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExamAttemptServiceTest {

    @Mock
    private ExamAttemptRepository examAttemptRepository;

    @Mock
    private UserTrainingService userTrainingService;

    @InjectMocks
    private ExamAttemptService examAttemptService;

    private ExamAttemptDTO examAttemptDTO;
    private ExamAttempt examAttempt;
    private UserTraining userTraining;

    @BeforeEach
    void setUp() {
        examAttemptDTO = new ExamAttemptDTO(null, null, null, 0, 1); // Assuming userTrainingId is needed
        examAttempt = new ExamAttempt();
        userTraining = new UserTraining();
    }

    @Test
    void save_ShouldThrowException_WhenUserTrainingIdIsZero() {
        ExamAttemptDTO invalidDTO = new ExamAttemptDTO(null, null, null, 0, 0);
        assertThrows(MissingArgumentException.class, () -> examAttemptService.save(invalidDTO));
    }

    @Test
    void save_ShouldSaveExamAttempt_WhenValidDTOIsProvided() {
        when(userTrainingService.findById(1)).thenReturn(userTraining);
        doAnswer(i->null).when(examAttemptRepository).save(any(ExamAttempt.class));

        examAttemptService.save(examAttemptDTO);

        verify(userTrainingService, times(1)).findById(1);
        verify(examAttemptRepository, times(1)).save(any(ExamAttempt.class));
    }

    @Test
    void findAll_ShouldReturnListOfExamAttempts() {
        when(examAttemptRepository.findAll()).thenReturn(List.of(examAttempt));
        List<ExamAttempt> result = examAttemptService.findAll();
        assertEquals(1, result.size());
        verify(examAttemptRepository, times(1)).findAll();
    }
}
