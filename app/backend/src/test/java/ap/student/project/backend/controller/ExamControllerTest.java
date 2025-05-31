package ap.student.project.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import ap.student.project.backend.dto.ExamDTO;
import ap.student.project.backend.dto.ExamResultDTO;
import ap.student.project.backend.dto.ExamStartResponseDTO;
import ap.student.project.backend.dto.ExamSubmissionDTO;
import ap.student.project.backend.dto.QuestionDTO;
import ap.student.project.backend.entity.Exam;
import ap.student.project.backend.entity.Question;
import ap.student.project.backend.service.ExamService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ExamControllerTest {
    
    @Mock
    private ExamService examService;

    @InjectMocks
    private ExamController examController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(examController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void getExams_ShouldReturnListOfExams() throws Exception {
        List<Exam> exams = Arrays.asList(new Exam(), new Exam());
        when(examService.findAll()).thenReturn(exams);

        mockMvc.perform(get("/exams")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(examService).findAll();
    }

    @Test
    void addExam_ShouldCreateNewExam() throws Exception {
        ExamDTO examDTO = new ExamDTO(70, 3, 60, 10, 1);
        Exam savedExam = new Exam();
        when(examService.save(any(ExamDTO.class))).thenReturn(savedExam);

        mockMvc.perform(post("/exams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(examDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(examService).save(any(ExamDTO.class));
    }

    @Test
    void getExamById_ShouldReturnExam() throws Exception {
        int examId = 1;
        Exam exam = new Exam();
        when(examService.findById(examId)).thenReturn(exam);

        mockMvc.perform(get("/exams/{id}", examId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(examService, times(2)).findById(examId);
    }

    @Test
    void updateExam_ShouldUpdateExistingExam() throws Exception {
        int examId = 1;
        ExamDTO examDTO = new ExamDTO(80, 2, 90, 15, 2);
        Exam updatedExam = new Exam();
        when(examService.update(examId, examDTO)).thenReturn(updatedExam);

        mockMvc.perform(put("/exams/{id}", examId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(examDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(examService).update(examId, examDTO);
    }

    @Test
    void getQuestions_ShouldReturnExamQuestions() throws Exception {
        int examId = 1;
        List<Question> questions = Arrays.asList(new Question(), new Question());
        when(examService.findAllQuestionsByExamId(examId)).thenReturn(questions);

        mockMvc.perform(get("/exams/{id}/questions", examId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(examService).findAllQuestionsByExamId(examId);
    }

    @Test
    void addQuestion_ShouldAddQuestionToExam() throws Exception {
        int examId = 1;
        QuestionDTO questionDTO = new QuestionDTO(Map.of(), List.of());
        doNothing().when(examService).addQuestion(examId, questionDTO);

        mockMvc.perform(post("/exams/{id}/questions", examId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(questionDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(examService).addQuestion(examId, questionDTO);
    }

    @Test
    void deleteQuestion_ShouldDeleteExamQuestions() throws Exception {
        int examId = 1;
        doNothing().when(examService).deleteQuestions(examId);

        mockMvc.perform(delete("/exams/{id}/questions", examId))
                .andExpect(status().isOk());

        verify(examService).deleteQuestions(examId);
    }

    @Test
    void submitExam_ShouldEvaluateAndReturnResult() throws Exception {
        ExamSubmissionDTO submissionDTO = new ExamSubmissionDTO(1, 1, List.of());
        ExamResultDTO resultDTO = new ExamResultDTO(85, true);
        when(examService.evaluateExam(any(ExamSubmissionDTO.class))).thenReturn(resultDTO);

        mockMvc.perform(post("/exams/submit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(submissionDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(examService).evaluateExam(any(ExamSubmissionDTO.class));
    }

    @Test
    void startExam_ShouldReturnExamWithRandomizedQuestions() throws Exception {
        int examId = 1;
        int userId = 123;
        ExamStartResponseDTO examStartResponse = new ExamStartResponseDTO(null, null);
        when(examService.startExamWithTimer(examId, userId)).thenReturn(examStartResponse);

        mockMvc.perform(get("/exams/{id}/start", examId)
                        .param("userId", String.valueOf(userId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(examService).startExamWithTimer(examId, userId);
    }

    @Test
    void getRemainingTime_ShouldReturnRemainingTimeInSeconds() throws Exception {
        int examId = 1;
        int userId = 123;
        int remainingSeconds = 1800;
        when(examService.getRemainingTimeInSeconds(examId, userId)).thenReturn(remainingSeconds);

        mockMvc.perform(get("/exams/{id}/time", examId)
                        .param("userId", String.valueOf(userId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.remainingSeconds").value(remainingSeconds));

        verify(examService).getRemainingTimeInSeconds(examId, userId);
    }
}
