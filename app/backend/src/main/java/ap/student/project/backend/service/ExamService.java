package ap.student.project.backend.service;

import ap.student.project.backend.dao.ExamRepository;
import ap.student.project.backend.dao.QuestionOptionRepository;
import ap.student.project.backend.dao.QuestionRepository;
import ap.student.project.backend.dto.ExamAnswerDTO;
import ap.student.project.backend.dto.ExamDTO;
import ap.student.project.backend.dto.ExamResultDTO;
import ap.student.project.backend.dto.ExamSubmissionDTO;
import ap.student.project.backend.dto.QuestionDTO;
import ap.student.project.backend.entity.Exam;
import ap.student.project.backend.entity.QuestionOption;
import ap.student.project.backend.entity.Training;
import ap.student.project.backend.entity.Question;
import ap.student.project.backend.exceptions.MissingArgumentException;
import ap.student.project.backend.exceptions.NotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    /**
     * Constructs a new ExamService with the required repositories and services.
     *
     * @param examRepository Repository for Exam entity operations
     * @param questionRepository Repository for Question entity operations
     * @param trainingService Service for training-related operations
     * @param questionOptionRepository Repository for QuestionOption entity operations
     */
    public ExamService(ExamRepository examRepository, QuestionRepository questionRepository, TrainingService trainingService, QuestionOptionRepository questionOptionRepository) {
        this.examRepository = examRepository;
        this.questionRepository = questionRepository;
        this.trainingService = trainingService;
        this.questionOptionRepository = questionOptionRepository;
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
    public ExamResultDTO evaluateExam(ExamSubmissionDTO submissionDTO) throws NotFoundException {
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
        
        return new ExamResultDTO(score, passed);
    }
}
