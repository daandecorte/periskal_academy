package ap.student.project.backend.service;

import ap.student.project.backend.dao.UserExamRepository;
import ap.student.project.backend.dto.UserExamDTO;
import ap.student.project.backend.entity.Exam;
import ap.student.project.backend.entity.User;
import ap.student.project.backend.entity.UserExam;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserExamServiceTest {
    private UserExamRepository userExamRepository;
    private UserService userService;
    private ExamService examService;
    private UserExamService userExamService;

    @BeforeEach
    void setUp() {
        userExamRepository = mock(UserExamRepository.class);
        userService = mock(UserService.class);
        examService = mock(ExamService.class);
        userExamService = new UserExamService(userExamRepository, userService, examService);
    }

    @Test
    void save_shouldSaveUserExam() {
        User user = new User();
        user.setId(1);
        Exam exam = new Exam();
        exam.setId(5);
        UserExamDTO dto = new UserExamDTO(5, 1, null);

        when(userService.findById(1)).thenReturn(user);
        when(examService.findById(5)).thenReturn(exam);

        userExamService.save(dto);

        ArgumentCaptor<UserExam> captor = ArgumentCaptor.forClass(UserExam.class);
        verify(userExamRepository, times(1)).save(captor.capture());
        UserExam savedUserExam = captor.getValue();

        assertEquals(1, savedUserExam.getUser().getId());
        assertEquals(5, savedUserExam.getExam().getId());
    }

    @Test
    void findAll_shouldReturnAllUserExams() {
        UserExam ue1 = new UserExam();
        UserExam ue2 = new UserExam();
        when(userExamRepository.findAll()).thenReturn(Arrays.asList(ue1, ue2));

        List<UserExam> result = userExamService.findAll();

        assertEquals(2, result.size());
        verify(userExamRepository, times(1)).findAll();
    }
}
