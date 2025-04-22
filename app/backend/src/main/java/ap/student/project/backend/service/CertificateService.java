package ap.student.project.backend.service;

import ap.student.project.backend.dao.CertificateRepository;
import ap.student.project.backend.dto.CertificateDTO;
import ap.student.project.backend.entity.Certificate;
import ap.student.project.backend.entity.Training;
import ap.student.project.backend.entity.User;
import ap.student.project.backend.entity.UserCertificate;
import ap.student.project.backend.exceptions.MissingArgumentException;
import ap.student.project.backend.exceptions.NotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CertificateService {
    private final CertificateRepository certificateRepository;
    private final TrainingService trainingService;
    public CertificateService(CertificateRepository certificateRepository, TrainingService trainingService) {
        this.certificateRepository = certificateRepository;
        this.trainingService = trainingService;
    }
    public List<Certificate> getAllCertificates() {
        return certificateRepository.findAll();
    }
    public Certificate save(CertificateDTO certificateDTO) {
        Certificate certificate = new Certificate();
        if(certificateDTO.training_id()==0) {
            throw new MissingArgumentException("training_id is missing");
        }
        Training training = this.trainingService.findById(certificateDTO.training_id());

        certificate.setTraining(training);

        BeanUtils.copyProperties(certificateDTO, certificate);

        return this.certificateRepository.save(certificate);
    }
    public Certificate findById(int id) {
        Certificate certificate = this.certificateRepository.findById(id).orElse(null);
        if (certificate == null) {
            throw new NotFoundException("Certificate with id " + id + " not found");
        }
        return certificate;
    }

    public List<Certificate> findByTrainingId(int id) {
        Training training = this.trainingService.findById(id);
        if(training.getCertificates().isEmpty()) {
            throw new NotFoundException("Training with id " + id + " has no certificates");
        }
        return training.getCertificates();
    }
}
