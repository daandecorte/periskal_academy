package ap.student.project.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import ap.student.project.backend.dto.ExamAttemptDTO;
import ap.student.project.backend.entity.ExamAttempt;
import ap.student.project.backend.entity.ExamStatusType;
import ap.student.project.backend.service.ExamAttemptService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ExamAttemptControllerTest {

    @Mock
    private ExamAttemptService examAttemptService;

    @InjectMocks
    private ExamAttemptController examAttemptController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(examAttemptController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
    }
    
    @Test
    void getExamAttempts_ShouldReturnListOfExamAttempts() throws Exception {
        List<ExamAttempt> examAttempts = Arrays.asList(new ExamAttempt(), new ExamAttempt());
        when(examAttemptService.findAll()).thenReturn(examAttempts);

        mockMvc.perform(get("/exam_attempts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(examAttemptService).findAll();
    }

    @Test
    void createExamAttempt_ShouldCreateNewExamAttempt() throws Exception {
        ExamAttemptDTO examAttemptDTO = new ExamAttemptDTO(
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1),
                ExamStatusType.PASSED,
                85,
                1
        );
        examAttemptService.save(any(ExamAttemptDTO.class));

        mockMvc.perform(post("/exam_attempts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(examAttemptDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(examAttemptService).save(any(ExamAttemptDTO.class));
    }
}
