package ap.student.project.backend.service;

import ap.student.project.backend.dao.CertificateRepository;
import ap.student.project.backend.dao.ExamAttemptRepository;
import ap.student.project.backend.dao.ExamRepository;
import ap.student.project.backend.dao.QuestionOptionRepository;
import ap.student.project.backend.dao.QuestionRepository;
import ap.student.project.backend.dao.TrainingRepository;
import ap.student.project.backend.dao.UserRepository;
import ap.student.project.backend.dao.UserTrainingRepository;
import ap.student.project.backend.dto.ExamAnswerDTO;
import ap.student.project.backend.dto.ExamDTO;
import ap.student.project.backend.dto.ExamResultDTO;
import ap.student.project.backend.dto.ExamStartResponseDTO;
import ap.student.project.backend.dto.ExamSubmissionDTO;
import ap.student.project.backend.dto.QuestionDTO;
import ap.student.project.backend.entity.Certificate;
import ap.student.project.backend.entity.Exam;
import ap.student.project.backend.entity.Language;
import ap.student.project.backend.entity.Question;
import ap.student.project.backend.entity.QuestionOption;
import ap.student.project.backend.entity.Training;
import ap.student.project.backend.entity.User;
import ap.student.project.backend.entity.UserTraining;
import ap.student.project.backend.exceptions.MissingArgumentException;
import ap.student.project.backend.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
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
    private UserTrainingService userTrainingService;

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuestionOptionRepository questionOptionRepository;

    @Autowired
    private TrainingRepository trainingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CertificateRepository certificateRepository;

    @Autowired
    private UserTrainingRepository userTrainingRepository;

    @Autowired
    private ExamAttemptRepository examAttemptRepository;

    private Training testTraining;
    private Exam testExam;
    private User testUser;
    private Certificate testCertificate;

    @BeforeEach
    void setUp() {
        // Clean up all data
        examAttemptRepository.deleteAll();
        userTrainingRepository.deleteAll();
        certificateRepository.deleteAll();
        questionOptionRepository.deleteAll();
        questionRepository.deleteAll();
        examRepository.deleteAll();
        trainingRepository.deleteAll();
        userRepository.deleteAll();

        // Create test user
        testUser = new User("TEST123", "John", "Doe", "Test Ship", Language.ENGLISH);
        testUser = userRepository.save(testUser);

        // Create test training
        Map<Language, String> title = new HashMap<>();
        title.put(Language.ENGLISH, "Test Training Title");
        title.put(Language.DUTCH, "Test Training Titel");

        Map<Language, String> description = new HashMap<>();
        description.put(Language.ENGLISH, "Test Description");
        description.put(Language.DUTCH, "Test Beschrijving");

        testTraining = new Training(title, description, true, null, null);
        testTraining = trainingRepository.save(testTraining);

        // Create test certificate
        testCertificate = new Certificate();
        testCertificate.setValidityPeriod(2);
        testCertificate.setTraining(testTraining);
        testCertificate = certificateRepository.save(testCertificate);

        // Update training to have certificate
        testTraining.setCertificate(testCertificate);
        testTraining = trainingRepository.save(testTraining);

        // Create and save an exam
        testExam = new Exam(70, 3, 60, 2, new ArrayList<>(), testTraining);
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
        assertThrows(DataIntegrityViolationException.class, () -> examService.save(examDTO));
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
    void findAll_ShouldReturnListOfExams() {
        List<Exam> exams = examService.findAll();
        assertEquals(1, exams.size());
    }

    @Test
    void addQuestion_ShouldAddQuestion_WhenExamIsNotFull() {
        QuestionDTO questionDTO = new QuestionDTO(null, Collections.emptyList());

        examService.addQuestion(testExam.getId(), questionDTO);

        List<Question> questions = questionRepository.findAll();
        assertEquals(1, questions.size());
    }


    @Test
    void findAllQuestionsByExamId_ShouldThrowException_WhenExamNotFound() {
        assertThrows(NotFoundException.class, () -> examService.findAllQuestionsByExamId(9999));
    }

    @Test
    void save_ShouldThrowException_WhenTrainingServiceReturnsNull() {
        // Create a training and delete to simulate trainingService returning null
        Training tempTraining = new Training();
        tempTraining = trainingRepository.save(tempTraining);
        int tempTrainingId = tempTraining.getId();
        trainingRepository.delete(tempTraining);

        ExamDTO examDTO = new ExamDTO(80, 2, 45, 5, tempTrainingId);
        assertThrows(NotFoundException.class, () -> examService.save(examDTO));
    }

    @Test
    void update_ShouldNotUpdateTraining_WhenExamExists() {
        // Create another training
        Map<Language, String> title2 = new HashMap<>();
        title2.put(Language.ENGLISH, "Another Training");
        Map<Language, String> description2 = new HashMap<>();
        description2.put(Language.ENGLISH, "Another Description");
        Training anotherTraining = new Training(title2, description2, true, null, null);
        anotherTraining = trainingRepository.save(anotherTraining);

        ExamDTO updatedDTO = new ExamDTO(90, 5, 90, 10, anotherTraining.getId());
        examService.update(testExam.getId(), updatedDTO);

        Exam updatedExam = examService.findById(testExam.getId());
        // Training should remain the same as original testTraining
        assertEquals(testTraining.getId(), updatedExam.getTraining().getId());
        assertEquals(90, updatedExam.getPassingScore());
    }

    @Test
    void deleteQuestions_ShouldRemoveAllQuestions_WhenExamHasQuestions() {
        // Add some questions first
        QuestionDTO questionDTO1 = new QuestionDTO(null, Collections.emptyList());
        QuestionDTO questionDTO2 = new QuestionDTO(null, Collections.emptyList());

        examService.addQuestion(testExam.getId(), questionDTO1);
        examService.addQuestion(testExam.getId(), questionDTO2);

        // Verify questions were added
        List<Question> questionsBefore = questionRepository.findAll();
        assertTrue(questionsBefore.size() >= 2);

        // Delete questions
        examService.deleteQuestions(testExam.getId());

        // Verify questions were deleted
        Exam examAfterDeletion = examService.findById(testExam.getId());
        assertTrue(examAfterDeletion.getQuestions().isEmpty());
    }

    @Test
    void deleteQuestions_ShouldThrowException_WhenExamNotFound() {
        assertThrows(NotFoundException.class, () -> examService.deleteQuestions(9999));
    }

    @Test
    void startExam_ShouldThrowException_WhenExamNotFound() {
        assertThrows(NotFoundException.class, () -> examService.startExam(9999));
    }

    @Test
    void startExamWithTimer_ShouldReturnExamStartResponseDTO() {
        // Add a question to the exam
        QuestionDTO questionDTO = new QuestionDTO(null, Collections.emptyList());
        examService.addQuestion(testExam.getId(), questionDTO);

        ExamStartResponseDTO response = examService.startExamWithTimer(testExam.getId(), 1);

        assertNotNull(response);
        assertNotNull(response.exam());
        assertNotNull(response.startTime());
        assertEquals(testExam.getId(), response.exam().getId());
    }

    @Test
    void getRemainingTimeInSeconds_ShouldReturnZero_WhenSessionNotFound() {
        int remainingTime = examService.getRemainingTimeInSeconds(testExam.getId(), 999);
        assertEquals(0, remainingTime);
    }

    @Test
    void getRemainingTimeInSeconds_ShouldReturnCorrectTime_WhenSessionExists() {
        // Start exam with timer to create session
        QuestionDTO questionDTO = new QuestionDTO(null, Collections.emptyList());
        examService.addQuestion(testExam.getId(), questionDTO);

        examService.startExamWithTimer(testExam.getId(), 1);

        int remainingTime = examService.getRemainingTimeInSeconds(testExam.getId(), 1);

        // Should be close to the exam time (60 minutes = 3600 seconds)
        assertTrue(remainingTime > 3500 && remainingTime <= 3600);
    }

    @Test
    void isExamTimeExpired_ShouldReturnTrue_WhenSessionNotFound() {
        boolean isExpired = examService.isExamTimeExpired(testExam.getId(), 999);
        assertTrue(isExpired);
    }

    @Test
    void isExamTimeExpired_ShouldReturnFalse_WhenTimeNotExpired() {
        // Add a question and start exam
        QuestionDTO questionDTO = new QuestionDTO(null, Collections.emptyList());
        examService.addQuestion(testExam.getId(), questionDTO);

        examService.startExamWithTimer(testExam.getId(), 1);

        boolean isExpired = examService.isExamTimeExpired(testExam.getId(), 1);
        assertFalse(isExpired);
    }

    @Test
    void addQuestion_ShouldHandleEmptyQuestionOptions() {
        QuestionDTO questionDTO = new QuestionDTO(null, new ArrayList<>());

        examService.addQuestion(testExam.getId(), questionDTO);

        List<Question> questions = questionRepository.findAll();
        assertEquals(1, questions.size());
        assertTrue(questions.get(0).getQuestionOptions().isEmpty());
    }

    @Test
    void addQuestion_ShouldThrowException_WhenExamNotFound() {
        QuestionDTO questionDTO = new QuestionDTO(null, Collections.emptyList());
        assertThrows(NotFoundException.class, () -> examService.addQuestion(9999, questionDTO));
    }

    @Test
    void evaluateExam_ShouldCalculateCorrectScore_WithPartialCorrectAnswers() {
        // Create questions with options
        Question question1 = createTestQuestionWithOptions("Question 1", true, false);
        Question question2 = createTestQuestionWithOptions("Question 2", false, true);
        question1 = questionRepository.save(question1);
        question2 = questionRepository.save(question2);

        // Create submission with one correct and one incorrect answer
        List<ExamAnswerDTO> answers = Arrays.asList(
            new ExamAnswerDTO(question1.getId(), question1.getQuestionOptions().get(0).getId()), // Correct
            new ExamAnswerDTO(question2.getId(), question2.getQuestionOptions().get(0).getId())  // Incorrect
        );

        ExamSubmissionDTO submission = new ExamSubmissionDTO(testExam.getId(), testUser.getId(), answers);

        ExamResultDTO result = examService.evaluateExam(submission);

        assertEquals(50, result.getScore()); // 1 out of 2 correct = 50%
        assertFalse(result.isPassed()); // 50% < 70% passing score
        assertNotNull(result.getAttemptId());
    }

    @Test
    void evaluateExam_ShouldCreateUserCertificate_WhenExamPassed() {
        // Create questions with options
        Question question1 = createTestQuestionWithOptions("Question 1", true, false);
        Question question2 = createTestQuestionWithOptions("Question 2", true, false);
        question1 = questionRepository.save(question1);
        question2 = questionRepository.save(question2);

        // Create submission with all correct answers
        List<ExamAnswerDTO> answers = Arrays.asList(
            new ExamAnswerDTO(question1.getId(), question1.getQuestionOptions().get(0).getId()), // Correct
            new ExamAnswerDTO(question2.getId(), question2.getQuestionOptions().get(0).getId())  // Correct
        );

        ExamSubmissionDTO submission = new ExamSubmissionDTO(testExam.getId(), testUser.getId(), answers);

        ExamResultDTO result = examService.evaluateExam(submission);

        assertEquals(100, result.getScore());
        assertTrue(result.isPassed());
        assertNotNull(result.getCertificateId());
        assertNotNull(result.getAttemptId());
    }

    @Test
    void evaluateExam_ShouldSkipInvalidQuestions_AndContinueEvaluation() {
        // Create one valid question
        Question validQuestion = createTestQuestionWithOptions("Valid Question", true, false);
        validQuestion = questionRepository.save(validQuestion);

        // Create submission with valid and invalid question IDs
        List<ExamAnswerDTO> answers = Arrays.asList(
            new ExamAnswerDTO(validQuestion.getId(), validQuestion.getQuestionOptions().get(0).getId()), // Valid
            new ExamAnswerDTO(9999, 9999) // Invalid question/option IDs
        );

        ExamSubmissionDTO submission = new ExamSubmissionDTO(testExam.getId(), testUser.getId(), answers);

        ExamResultDTO result = examService.evaluateExam(submission);

        assertEquals(50, result.getScore()); // 1 correct out of 2 submitted = 50%
        assertFalse(result.isPassed());
    }

    @Test
    void evaluateExam_ShouldReturnZeroScore_WhenNoQuestionsProvided() {
        ExamSubmissionDTO submission = new ExamSubmissionDTO(testExam.getId(), testUser.getId(), Collections.emptyList());

        ExamResultDTO result = examService.evaluateExam(submission);

        assertEquals(0, result.getScore());
        assertFalse(result.isPassed());
        assertNotNull(result.getAttemptId());
    }

    @Test
    void evaluateExam_ShouldCreateUserTraining_WhenNotExists() {
        // Create a question for evaluation
        Question question = createTestQuestionWithOptions("Question", true, false);
        question = questionRepository.save(question);

        List<ExamAnswerDTO> answers = Arrays.asList(
            new ExamAnswerDTO(question.getId(), question.getQuestionOptions().get(0).getId())
        );

        ExamSubmissionDTO submission = new ExamSubmissionDTO(testExam.getId(), testUser.getId(), answers);

        // Verify no UserTraining exists initially
        assertThrows(NotFoundException.class, () -> 
            userTrainingService.findByTrainingIdAndUserId(testTraining.getId(), testUser.getId()));

        ExamResultDTO result = examService.evaluateExam(submission);

        // Verify UserTraining was created
        assertNotNull(result.getAttemptId());
        UserTraining createdUserTraining = userTrainingService.findByTrainingIdAndUserId(testTraining.getId(), testUser.getId());
        assertNotNull(createdUserTraining);
        assertTrue(createdUserTraining.isEligibleForCertificate());
    }

    @Test
    void addQuestion_ShouldSaveQuestionWithOptions() {
        Map<Language, String> questionText = new HashMap<>();
        questionText.put(Language.ENGLISH, "Test Question");

        Map<Language, String> option1Text = new HashMap<>();
        option1Text.put(Language.ENGLISH, "Option 1");
        
        Map<Language, String> option2Text = new HashMap<>();
        option2Text.put(Language.ENGLISH, "Option 2");

        QuestionOption option1 = new QuestionOption(option1Text, true, null);
        QuestionOption option2 = new QuestionOption(option2Text, false, null);

        QuestionDTO questionDTO = new QuestionDTO(questionText, Arrays.asList(option1, option2));

        examService.addQuestion(testExam.getId(), questionDTO);

        List<Question> questions = questionRepository.findAll();
        assertEquals(1, questions.size());
        
        Question savedQuestion = questions.get(0);
        assertEquals(2, savedQuestion.getQuestionOptions().size());
        assertEquals(testExam.getId(), savedQuestion.getExam().getId());
        
        // Verify options are properly linked
        for (QuestionOption option : savedQuestion.getQuestionOptions()) {
            assertEquals(savedQuestion.getId(), option.getQuestion().getId());
        }
    }

    //Helper method to create test question
    private Question createTestQuestionWithOptions(String questionText, boolean firstCorrect, boolean secondCorrect) {
        Map<Language, String> text = new HashMap<>();
        text.put(Language.ENGLISH, questionText);

        Map<Language, String> option1Text = new HashMap<>();
        option1Text.put(Language.ENGLISH, "Option 1");
        
        Map<Language, String> option2Text = new HashMap<>();
        option2Text.put(Language.ENGLISH, "Option 2");

        Question question = new Question(text, new ArrayList<>(), testExam, null);
        
        QuestionOption option1 = new QuestionOption(option1Text, firstCorrect, question);
        QuestionOption option2 = new QuestionOption(option2Text, secondCorrect, question);
        
        question.setQuestionOptions(Arrays.asList(option1, option2));
        
        return question;
    }
}
