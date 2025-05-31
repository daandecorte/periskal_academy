package ap.student.project.backend.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;

import ap.student.project.backend.dto.ContentDTO;
import ap.student.project.backend.dto.ModuleDTO;
import ap.student.project.backend.dto.QuestionDTO;
import ap.student.project.backend.entity.Content;
import ap.student.project.backend.entity.ContentType;
import ap.student.project.backend.entity.Exam;
import ap.student.project.backend.entity.Language;
import ap.student.project.backend.entity.Module;
import ap.student.project.backend.entity.Question;
import ap.student.project.backend.service.ModuleService;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ModuleControllerTest {

    @Mock
    private ModuleService moduleService;

    @InjectMocks
    private ModuleController moduleController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private ModuleDTO moduleDTO;
    private Module module;
    private ContentDTO contentDTO;
    private Content content;
    private QuestionDTO questionDTO;
    private Question question;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(moduleController).build();
        objectMapper = new ObjectMapper();

        // Setup test data
        Map<Language, String> title = Map.of(Language.ENGLISH, "Test Module");
        Map<Language, String> description = Map.of(Language.ENGLISH, "Test Description");
        moduleDTO = new ModuleDTO(title, description, new ArrayList<>(), 1);

        Map<Language, String> contentRef = Map.of(Language.ENGLISH, "Test Content");
        contentDTO = new ContentDTO(ContentType.TEXT, contentRef);

        Map<Language, String> questionText = Map.of(Language.ENGLISH, "Test Question?");
        questionDTO = new QuestionDTO(questionText, new ArrayList<>());
    }

    @Test
    void getAllModules_ShouldReturnAllModules() throws Exception {
        List<Module> modules = Arrays.asList(new Module(), new Module());
        when(moduleService.getAllModules()).thenReturn(modules);

        mockMvc.perform(get("/modules")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(moduleService).getAllModules();
    }

    @Test
    void getAllModules_ShouldReturnOkResponse() {
        List<Module> modules = Arrays.asList(new Module(), new Module());
        when(moduleService.getAllModules()).thenReturn(modules);

        ResponseEntity response = moduleController.getAllModules();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(modules, response.getBody());
        verify(moduleService).getAllModules();
    }

    @Test
    void addModule_ShouldCreateModule() throws Exception {
        when(moduleService.save(any(ModuleDTO.class))).thenReturn(module);

        mockMvc.perform(post("/modules")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(moduleDTO)))
                .andExpect(status().isCreated());

        verify(moduleService).save(any(ModuleDTO.class));
    }

    @Test
    void addModule_ShouldReturnCreatedResponse() {
        when(moduleService.save(any(ModuleDTO.class))).thenReturn(module);

        ResponseEntity response = moduleController.addModule(moduleDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(module, response.getBody());
        verify(moduleService).save(moduleDTO);
    }

    @Test
    void getModuleById_ShouldReturnModule() throws Exception {
        Integer moduleId = 1;
        when(moduleService.getModuleById(moduleId)).thenReturn(module);

        mockMvc.perform(get("/modules/{id}", moduleId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(moduleService).getModuleById(moduleId);
    }

    @Test
    void getModuleById_ShouldReturnOkResponse() {
        Integer moduleId = 1;
        when(moduleService.getModuleById(moduleId)).thenReturn(module);

        ResponseEntity response = moduleController.getModuleById(moduleId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(module, response.getBody());
        verify(moduleService).getModuleById(moduleId);
    }

    @Test
    void updateModule_ShouldUpdateModule() throws Exception {
        Integer moduleId = 1;
        when(moduleService.updateModule(eq(moduleId), any(ModuleDTO.class))).thenReturn(module);

        mockMvc.perform(put("/modules/{id}", moduleId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(moduleDTO)))
                .andExpect(status().isOk());

        verify(moduleService).updateModule(eq(moduleId), any(ModuleDTO.class));
    }

    @Test
    void updateModule_ShouldReturnOkResponse() {
        Integer moduleId = 1;
        when(moduleService.updateModule(eq(moduleId), any(ModuleDTO.class))).thenReturn(module);

        ResponseEntity response = moduleController.updateModule(moduleId, moduleDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(module, response.getBody());
        verify(moduleService).updateModule(moduleId, moduleDTO);
    }

    @Test
    void deleteModule_ShouldDeleteModule() throws Exception {
        Integer moduleId = 1;
        doNothing().when(moduleService).deleteModule(moduleId);

        mockMvc.perform(delete("/modules/{id}", moduleId))
                .andExpect(status().isOk());

        verify(moduleService).deleteModule(moduleId);
    }

    @Test
    void deleteModule_ShouldReturnOkResponse() {
        Integer moduleId = 1;
        doNothing().when(moduleService).deleteModule(moduleId);

        ResponseEntity response = moduleController.deleteModule(moduleId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
        verify(moduleService).deleteModule(moduleId);
    }
    
    @Test
    void addContent_ShouldAddContent() throws Exception {
        Integer moduleId = 1;
        when(moduleService.addContent(eq(moduleId), any(ContentDTO.class))).thenReturn(content);

        mockMvc.perform(post("/modules/{id}/content", moduleId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(contentDTO)))
                .andExpect(status().isCreated());

        verify(moduleService).addContent(eq(moduleId), any(ContentDTO.class));
    }

    @Test
    void addContent_ShouldReturnCreatedResponse() {
        Integer moduleId = 1;
        when(moduleService.addContent(eq(moduleId), any(ContentDTO.class))).thenReturn(content);

        ResponseEntity response = moduleController.addContent(moduleId, contentDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(content, response.getBody());
        verify(moduleService).addContent(moduleId, contentDTO);
    }

    @Test
    void updateContent_ShouldUpdateContent() throws Exception {
        Integer moduleId = 1;
        Integer contentId = 2;
        when(moduleService.updateContent(eq(moduleId), eq(contentId), any(ContentDTO.class))).thenReturn(content);

        mockMvc.perform(put("/modules/{id}/content/{idContent}", moduleId, contentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(contentDTO)))
                .andExpect(status().isCreated());

        verify(moduleService).updateContent(eq(moduleId), eq(contentId), any(ContentDTO.class));
    }

    @Test
    void updateContent_ShouldReturnCreatedResponse() {
        Integer moduleId = 1;
        Integer contentId = 2;
        when(moduleService.updateContent(eq(moduleId), eq(contentId), any(ContentDTO.class))).thenReturn(content);

        ResponseEntity response = moduleController.updateContent(moduleId, contentId, contentDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(content, response.getBody());
        verify(moduleService).updateContent(moduleId, contentId, contentDTO);
    }

    @Test
    void deleteContent_ShouldDeleteContent() throws Exception {
        Integer moduleId = 1;
        Integer contentId = 2;
        doNothing().when(moduleService).deleteContent(moduleId, contentId);

        mockMvc.perform(delete("/modules/{id}/content/{idContent}", moduleId, contentId))
                .andExpect(status().isOk());

        verify(moduleService).deleteContent(moduleId, contentId);
    }

    @Test
    void deleteContent_ShouldReturnOkResponse() {
        Integer moduleId = 1;
        Integer contentId = 2;
        doNothing().when(moduleService).deleteContent(moduleId, contentId);

        ResponseEntity response = moduleController.deleteContent(moduleId, contentId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
        verify(moduleService).deleteContent(moduleId, contentId);
    }

    @Test
    void addQuestion_ShouldAddQuestion() throws Exception {
        int moduleId = 1;
        when(moduleService.addQuestion(eq(moduleId), any(QuestionDTO.class))).thenReturn(question);

        mockMvc.perform(post("/modules/{id}/questions", moduleId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(questionDTO)))
                .andExpect(status().isCreated());

        verify(moduleService).addQuestion(eq(moduleId), any(QuestionDTO.class));
    }

    @Test
    void addQuestion_ShouldReturnCreatedResponse() {
        int moduleId = 1;
        when(moduleService.addQuestion(eq(moduleId), any(QuestionDTO.class))).thenReturn(question);

        ResponseEntity response = moduleController.addQuestion(moduleId, questionDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(question, response.getBody());
        verify(moduleService).addQuestion(moduleId, questionDTO);
    }

    @Test
    void getQuestion_ShouldReturnQuestions() throws Exception {
        int moduleId = 1;
        List<Question> questions = Arrays.asList(new Question(), new Question());
        when(moduleService.getQuestionsByModuleId(moduleId)).thenReturn(questions);

        mockMvc.perform(get("/modules/{id}/questions", moduleId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(moduleService).getQuestionsByModuleId(moduleId);
    }

    @Test
    void getQuestion_ShouldReturnOkResponse() {
        int moduleId = 1;
        List<Question> questions = Arrays.asList(new Question(), new Question());
        when(moduleService.getQuestionsByModuleId(moduleId)).thenReturn(questions);

        ResponseEntity response = moduleController.getQuestion(moduleId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(questions, response.getBody());
        verify(moduleService).getQuestionsByModuleId(moduleId);
    }

    @Test
    void deleteQuestions_ShouldDeleteQuestions() throws Exception {
        int moduleId = 1;
        doNothing().when(moduleService).deleteQuestions(moduleId);

        mockMvc.perform(delete("/modules/{id}/questions", moduleId))
                .andExpect(status().isOk());

        verify(moduleService).deleteQuestions(moduleId);
    }

    @Test
    void deleteQuestions_ShouldReturnOkResponse() {
        int moduleId = 1;
        doNothing().when(moduleService).deleteQuestions(moduleId);

        ResponseEntity response = moduleController.deleteQuestions(moduleId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
        verify(moduleService).deleteQuestions(moduleId);
    }
}
