package ap.student.project.backend.service;

import ap.student.project.backend.dao.ExamRepository;
import ap.student.project.backend.dto.ExamDTO;
import ap.student.project.backend.entity.Exam;
import ap.student.project.backend.exceptions.NotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExamService {
    private final ExamRepository examRepository;

    public ExamService(ExamRepository examRepository) {
        this.examRepository = examRepository;
    }

    public void save(ExamDTO examDTO) {
        Exam exam = new Exam();
        BeanUtils.copyProperties(examDTO, exam);
        examRepository.save(exam);
    }

    public Exam findById(int id) {
        Exam exam = examRepository.findById(id).orElse(null);
        if (exam == null) {
            throw new NotFoundException("Exam with id " + id + " not found");
        }
        return exam;
    }

    public void update(int id, ExamDTO examDTO) {
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
}
