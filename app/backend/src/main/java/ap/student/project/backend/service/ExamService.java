package ap.student.project.backend.service;

import ap.student.project.backend.dao.ExamRepository;
import ap.student.project.backend.dao.QuestionRepository;
import ap.student.project.backend.dto.ExamDTO;
import ap.student.project.backend.dto.QuestionDTO;
import ap.student.project.backend.entity.Exam;
import ap.student.project.backend.entity.QuestionOption;
import ap.student.project.backend.entity.Training;
import ap.student.project.backend.entity.Question;
import ap.student.project.backend.exceptions.ListFullException;
import ap.student.project.backend.exceptions.MissingArgumentException;
import ap.student.project.backend.exceptions.NotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ExamService {
    private final ExamRepository examRepository;
    private final QuestionRepository questionRepository;
    private final TrainingService trainingService;

    public ExamService(ExamRepository examRepository, QuestionRepository questionRepository, TrainingService trainingService) {
        this.examRepository = examRepository;
        this.questionRepository = questionRepository;
        this.trainingService = trainingService;
    }

    public void save(ExamDTO examDTO) throws MissingArgumentException, NotFoundException {
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
        examRepository.save(exam);
    }

    public Exam findById(int id) throws NotFoundException {
        Exam exam = examRepository.findById(id).orElse(null);
        if (exam == null) {
            throw new NotFoundException("Exam with id " + id + " not found");
        }
        return exam;
    }

    public void update(int id, ExamDTO examDTO) throws NotFoundException {
        Exam exam = examRepository.findById(id).orElse(null);
        if (exam == null) {
            throw new NotFoundException("Exam with id " + id + " not found");
        }
        BeanUtils.copyProperties(examDTO, exam);
        examRepository.save(exam);
    }

    public List<Exam> findAll() {
        return examRepository.findAll();
    }

    public void delete(int id) {
        examRepository.deleteById(id);
    }
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
            if (exam.getQuestions().size() < exam.getQuestionAmount()) {
                questionRepository.save(question);
            } else {
                throw new ListFullException("Exam with id " + id + " has a question limit of " + exam.getQuestionAmount());
            }
        } catch (NotFoundException e) {
            throw new NotFoundException("Exam with id " + id + " not found");
        }
    }
    public List<Question> findAllQuestionsByExamId(int id) {
        try {
            Exam exam = this.findById(id);
            return exam.getQuestions();
        }
        catch (NotFoundException e) {
            throw new NotFoundException("Exam with id " + id + " not found");
        }
    }
}
