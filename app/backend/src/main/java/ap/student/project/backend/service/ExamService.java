package ap.student.project.backend.service;

import ap.student.project.backend.dao.ExamRepository;
import ap.student.project.backend.dao.QuestionOptionRepository;
import ap.student.project.backend.dao.QuestionRepository;
import ap.student.project.backend.dto.ExamAnswerDTO;
import ap.student.project.backend.dto.ExamAttemptDTO;
import ap.student.project.backend.dto.ExamDTO;
import ap.student.project.backend.dto.ExamResultDTO;
import ap.student.project.backend.dto.ExamSubmissionDTO;
import ap.student.project.backend.dto.QuestionDTO;
import ap.student.project.backend.dto.UserCertificateDTO;
import ap.student.project.backend.dto.UserTrainingDTO;
import ap.student.project.backend.entity.Certificate;
import ap.student.project.backend.entity.CertificateStatus;
import ap.student.project.backend.entity.Exam;
import ap.student.project.backend.entity.ExamStatusType;
import ap.student.project.backend.entity.QuestionOption;
import ap.student.project.backend.entity.Training;
import ap.student.project.backend.entity.User;
import ap.student.project.backend.entity.UserCertificate;
import ap.student.project.backend.entity.UserTraining;
import ap.student.project.backend.entity.Question;
import ap.student.project.backend.exceptions.MissingArgumentException;
import ap.student.project.backend.exceptions.NotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Service class for managing exam-related operations.
 * Handles creating, updating, and evaluating exams and their questions.
 */
@Service
public class ExamService {
    private final ExamRepository examRepository;
    private final QuestionRepository questionRepository;
    private final TrainingService trainingService;
    private final QuestionOptionRepository questionOptionRepository;
    private final UserCertificateService userCertificateService;
    private final CertificateService certificateService;
    private final UserService userService;
    private final ExamAttemptService examAttemptService;
    private final UserTrainingService userTrainingService;

    /**
     * Constructs a new ExamService with the required repositories and services.
     *
     * @param examRepository Repository for Exam entity operations
     * @param questionRepository Repository for Question entity operations
     * @param trainingService Service for training-related operations
     * @param questionOptionRepository Repository for QuestionOption entity operations
     * @param userCertificateService Service for UserCertificate-related operations
     * @param certificateService Service for certificate-related operations
     * @param userService Service for user-related operations
     */
    public ExamService(ExamRepository examRepository, QuestionRepository questionRepository, TrainingService trainingService, QuestionOptionRepository questionOptionRepository, UserCertificateService userCertificateService, UserCertificateService userCertificateService2, CertificateService certificateService, UserService userService, ExamAttemptService examAttemptService, UserTrainingService userTrainingService) {
        this.examRepository = examRepository;
        this.questionRepository = questionRepository;
        this.trainingService = trainingService;
        this.questionOptionRepository = questionOptionRepository;
        this.userCertificateService = userCertificateService;
        this.certificateService = certificateService;
        this.userService = userService;
        this.examAttemptService = examAttemptService;
        this.userTrainingService = userTrainingService;
    }

    /**
     * Creates and saves a new exam.
     *
     * @param examDTO Data transfer object containing exam information
     * @return The saved Exam entity
     * @throws MissingArgumentException If training_id is missing from the DTO
     * @throws NotFoundException If the training is not found
     */
    public Exam save(ExamDTO examDTO) throws MissingArgumentException, NotFoundException {
        Exam exam = new Exam();
        if(examDTO.trainingId()==0) {
            throw new MissingArgumentException("training_id is missing");
        }
        Training training = trainingService.findById(examDTO.trainingId());
        if(training ==null) {
            throw new NotFoundException("training with id" + examDTO.trainingId() +" not found");
        }
        BeanUtils.copyProperties(examDTO, exam);
        exam.setTraining(training);
        return examRepository.save(exam);
    }

    /**
     * Finds an exam by its ID.
     *
     * @param id The ID of the exam to find
     * @return The found Exam entity
     * @throws NotFoundException If no exam with the given ID exists
     */
    public Exam findById(int id) throws NotFoundException {
        Exam exam = examRepository.findById(id).orElse(null);
        if (exam == null) {
            throw new NotFoundException("Exam with id " + id + " not found");
        }
        return exam;
    }

    /**
     * Updates an existing exam with new information.
     *
     * @param id The ID of the exam to update
     * @param examDTO Data transfer object containing updated exam information
     * @throws NotFoundException If no exam with the given ID exists
     */
    public Exam update(int id, ExamDTO examDTO) throws NotFoundException {
        Exam exam = examRepository.findById(id).orElse(null);
        if (exam == null) {
            throw new NotFoundException("Exam with id " + id + " not found");
        }
        BeanUtils.copyProperties(examDTO, exam);
        examRepository.save(exam);
        return exam;
    }

    /**
     * Retrieves all exams in the system.
     *
     * @return A list of all Exam entities
     */
    public List<Exam> findAll() {
        return examRepository.findAll();
    }

    /**
     * Adds a new question to an existing exam.
     *
     * @param id The ID of the exam to add the question to
     * @param questionDTO Data transfer object containing question information
     * @throws NotFoundException If no exam with the given ID exists
     */
    @Transactional
    public void addQuestion(int id, QuestionDTO questionDTO) {
        try {
            Exam exam = this.findById(id);
            Question question = new Question();
            BeanUtils.copyProperties(questionDTO, question);
            question.setExam(exam);
            List<QuestionOption> questionOptions = questionDTO.questionOptions();
            for (QuestionOption questionOption : questionOptions) {
                questionOption.setQuestion(question);
            }
            question.setQuestionOptions(questionOptions);
            questionRepository.save(question);
        } catch (NotFoundException e) {
            throw new NotFoundException("Exam with id " + id + " not found");
        }
    }

    /**
     * Deletes the questions of an exam.
     *
     * @param id The ID of the exam to delete the question for
     * @throws NotFoundException If no exam with the given ID exists
     */
    @Transactional
    public void deleteQuestions(int id){
        Exam exam = this.findById(id);
        if(exam == null){
            throw new NotFoundException("Exam with id " + id + " not found");
        }
        for(Question question : exam.getQuestions()){
            questionOptionRepository.deleteAll(question.getQuestionOptions());
            questionRepository.delete(question);
        }
        exam.getQuestions().clear();
        examRepository.save(exam);
    }


    /**
     * Retrieves all questions for a specific exam.
     *
     * @param id The ID of the exam to get questions for
     * @return A list of Question entities associated with the exam
     * @throws NotFoundException If no exam with the given ID exists
     */
    public List<Question> findAllQuestionsByExamId(int id) {
        try {
            Exam exam = this.findById(id);
            return exam.getQuestions();
        }
        catch (NotFoundException e) {
            throw new NotFoundException("Exam with id " + id + " not found");
        }
    }

     /**
     * Evaluates an exam submission and calculates the score.
     *
     * @param submissionDTO Data transfer object containing the exam submission information
     * @return An ExamResultDTO containing the score and pass/fail status
     * @throws NotFoundException If the exam or any of its components are not found
     */
    @Transactional
    public ExamResultDTO evaluateExam(ExamSubmissionDTO submissionDTO) throws NotFoundException {
        //Get current time for exam attempt
        LocalDateTime startTime = LocalDateTime.now();

        // Find the exam
        Exam exam = findById(submissionDTO.getExamId());
        
        // Count correct answers
        int correctAnswers = 0;
        int totalQuestions = submissionDTO.getAnswers().size();
        
        for (ExamAnswerDTO answerDTO : submissionDTO.getAnswers()) {
            Optional<Question> questionOptional = questionRepository.findById(answerDTO.getQuestionId());
            if (questionOptional.isEmpty()) {
                continue; // Skip this answer if question not found
            }
            
            Optional<QuestionOption> optionOptional = questionOptionRepository.findById(answerDTO.getOptionId());
            if (optionOptional.isEmpty()) {
                continue; // Skip this answer if option not found
            }
            
            QuestionOption selectedOption = optionOptional.get();
            if (selectedOption.isCorrect()) {
                correctAnswers++;
            }
        }
        
        // Calculate score as percentage
        int score = (totalQuestions > 0) ? (correctAnswers * 100) / totalQuestions : 0;
        
        // Determine if passed
        boolean passed = score >= exam.getPassingScore();
        
        ExamResultDTO result = new ExamResultDTO(score, passed);

        // Create ExamAttempt
        try {
            // Find UserTraining
            Training training = exam.getTraining();
            User user = userService.findById(submissionDTO.getUserId());
            
            UserTraining userTraining = userTrainingService.findByTrainingIdAndUserId(training.getId(), user.getId());
            if (userTraining == null) {
                // TODO: create UserTraining if it doesn't exist
            }
            
            // Create ExamAttempt
            ExamAttemptDTO examAttemptDTO = new ExamAttemptDTO(
                startTime, // start time
                LocalDateTime.now(), // end time
                passed ? ExamStatusType.PASSED : ExamStatusType.FAILED,
                score,
                userTraining.getId()
            );
            
            examAttemptService.save(examAttemptDTO);
            
        } catch (Exception e) {
            // Log the error but don't fail the exam result
            System.err.println("Failed to create exam attempt record: " + e.getMessage());
            e.printStackTrace();
        }

        // If exam is passed, create UserCertificate
        if (passed) {
            try {
                // Get the exam to find the associated training
                Exam examById = findById(submissionDTO.getExamId());
                Training training = examById.getTraining();
                Certificate certificate = training.getCertificate();
                
                if (certificate != null) {
                    // Create UserCertificate
                    LocalDate issueDate = LocalDate.now();
                    LocalDate expiryDate = issueDate.plusMonths(certificate.getValidityPeriod());
                    
                    UserCertificateDTO userCertificateDTO = new UserCertificateDTO(
                        issueDate,
                        expiryDate,
                        CertificateStatus.VALID,
                        submissionDTO.getUserId(),
                        certificate.getId()
                    );
                    
                    UserCertificate userCertificate = userCertificateService.save(userCertificateDTO);
                    result.setCertificateId(userCertificate.getId());
                }
            } catch (Exception e) {
                // Log the error but don't fail the exam result
                System.err.println("Failed to create user certificate: " + e.getMessage());
            }
        }

        return result;
    }

    /**
     * Returns a copy of an exam that includes only randomly chosen questions. 
     * If the total available questions are fewer than or equal to the configured amount, all questions are included.
     *
     * @param id the ID of the exam to start
     * @return a copy of the Exam object with randomly selected questions
     * @throws NotFoundException if no exam with the given ID is found
     */
    public Exam startExam(int id) throws NotFoundException {
        Exam exam = findById(id);
        
        // Get all questions
        List<Question> allQuestions = exam.getQuestions();
        
        // Determine how many questions to select
        int questionsToSelect = Math.min(exam.getQuestionAmount(), allQuestions.size());
        
        // If all questions are needed or there is exactly the right amount, return as is
        if (questionsToSelect >= allQuestions.size()) {
            return exam;
        }
        
        // Randomly select questions
        List<Question> selectedQuestions = selectRandomQuestions(allQuestions, questionsToSelect);
        
        // Create a copy of the exam with only selected questions
        Exam examCopy = new Exam();
        BeanUtils.copyProperties(exam, examCopy);
        examCopy.setQuestions(selectedQuestions);
        
        return examCopy;
    }

    /**
     * Selects a random subset of questions from the provided list.
     *
     * @param allQuestions the full list of available questions
     * @param count the number of questions to select
     * @return a list containing randomly selected questions
     */
    private List<Question> selectRandomQuestions(List<Question> allQuestions, int count) {
        List<Question> questionsCopy = new ArrayList<>(allQuestions);
        Collections.shuffle(questionsCopy);
        return questionsCopy.subList(0, count);
    }
}
