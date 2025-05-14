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

/**
 * Service class for managing certificates.
 * Handles creating, retrieving, and managing certificate entities.
 */
@Service
public class CertificateService {
    private final CertificateRepository certificateRepository;
    private final TrainingService trainingService;
    
    /**
     * Constructs a new CertificateService with the required repositories and services.
     *
     * @param certificateRepository Repository for Certificate entity operations
     * @param trainingService Service for training-related operations
     */
    public CertificateService(CertificateRepository certificateRepository, TrainingService trainingService) {
        this.certificateRepository = certificateRepository;
        this.trainingService = trainingService;
    }
    
    /**
     * Retrieves all certificates in the system.
     *
     * @return A list of all Certificate entities
     */
    public List<Certificate> getAllCertificates() {
        return certificateRepository.findAll();
    }
    
    /**
     * Creates and saves a new certificate.
     *
     * @param certificateDTO Data transfer object containing certificate information
     * @return The saved Certificate entity
     * @throws MissingArgumentException If training_id is missing from the DTO
     * @throws NotFoundException If the training is not found
     */
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
    
    /**
     * Finds a certificate by its ID.
     *
     * @param id The ID of the certificate to find
     * @return The found Certificate entity
     * @throws NotFoundException If no certificate with the given ID exists
     */
    public Certificate findById(int id) {
        Certificate certificate = this.certificateRepository.findById(id).orElse(null);
        if (certificate == null) {
            throw new NotFoundException("Certificate with id " + id + " not found");
        }
        return certificate;
    }

    /**
     * Retrieves all certificates associated with a specific training.
     *
     * @param id The ID of the training to get certificates for
     * @return A list of Certificate entities associated with the training
     * @throws NotFoundException If the training is not found or has no certificates
     */
    public List<Certificate> findByTrainingId(int id) {
        Training training = this.trainingService.findById(id);
        if(training.getCertificates().isEmpty()) {
            throw new NotFoundException("Training with id " + id + " has no certificates");
        }
        return training.getCertificates();
    }
}
