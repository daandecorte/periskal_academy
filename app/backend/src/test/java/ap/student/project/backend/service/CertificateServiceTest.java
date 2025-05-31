package ap.student.project.backend.service;

import ap.student.project.backend.dao.CertificateRepository;
import ap.student.project.backend.dto.CertificateDTO;
import ap.student.project.backend.dto.TrainingDTO;
import ap.student.project.backend.entity.Certificate;
import ap.student.project.backend.entity.Training;
import ap.student.project.backend.exceptions.MissingArgumentException;
import ap.student.project.backend.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class CertificateServiceTest {

    @Autowired
    private CertificateService certificateService;

    @Autowired
    private TrainingService trainingService;

    @Autowired
    private CertificateRepository certificateRepository;

    private Training training;
    private Training anotherTraining;
    private TrainingDTO trainingDTO;
    private CertificateDTO certificateDTO;

    @BeforeEach
    void setUp() {
        // Create first training
        trainingDTO = new TrainingDTO(null, null, true, null, null);
        training = trainingService.save(trainingDTO);
        
        // Create second training for additional test cases
        TrainingDTO anotherTrainingDTO = new TrainingDTO(null, null, true, null, null);
        anotherTraining = trainingService.save(anotherTrainingDTO);
        
        certificateDTO = new CertificateDTO(
                training.getId(),
                2,
                50.0
        );
    }

    @Test
    void testSaveCertificateSuccess() {
        Certificate savedCertificate = certificateService.save(certificateDTO);

        assertThat(savedCertificate).isNotNull();
        assertThat(savedCertificate.getId()).isNotZero();
        assertThat(savedCertificate.getValidityPeriod()).isEqualTo(2);
        assertThat(savedCertificate.getPrice()).isEqualTo(50.0);
        assertThat(savedCertificate.getTraining().getId()).isEqualTo(training.getId());
    }

    @Test
    void testSaveCertificateMissingTrainingIdThrows() {
        certificateDTO = new CertificateDTO(0, 0, 0);
        assertThrows(MissingArgumentException.class, () -> certificateService.save(certificateDTO));
    }

    @Test
    void testSaveCertificateWithZeroTrainingIdThrowsMissingArgumentException() {
        CertificateDTO invalidDTO = new CertificateDTO(0, 2, 50.0);
        
        assertThrows(MissingArgumentException.class, 
                () -> certificateService.save(invalidDTO));
    }

    @Test
    void testSaveCertificateWithNegativeTrainingIdThrowsMissingArgumentException() {
        CertificateDTO invalidDTO = new CertificateDTO(-1, 2, 50.0);
        
        assertThrows(MissingArgumentException.class, 
                () -> certificateService.save(invalidDTO));
    }

    @Test
    void testSaveCertificateWithNonExistentTrainingThrowsNotFoundException() {
        CertificateDTO invalidDTO = new CertificateDTO(999, 2, 50.0);
        
        assertThrows(NotFoundException.class, 
                () -> certificateService.save(invalidDTO));
    }

    @Test
    void testSaveCertificateAlreadyExistsThrowsIllegalArgumentException() {
        // First save should succeed
        certificateService.save(certificateDTO);
        
        // Second save with same training_id should throw exception
        CertificateDTO duplicateDTO = new CertificateDTO(
                training.getId(),
                3,
                75.0
        );
        
        assertThrows(DataIntegrityViolationException.class, 
                () -> certificateService.save(duplicateDTO));
    }

    @Test
    void testSaveCertificateWithDifferentTrainingIds() {
        // Save certificate for first training
        Certificate firstCertificate = certificateService.save(certificateDTO);
        
        // Save certificate for second training - should succeed
        CertificateDTO secondCertificateDTO = new CertificateDTO(
                anotherTraining.getId(),
                3,
                75.0
        );
        Certificate secondCertificate = certificateService.save(secondCertificateDTO);
        
        assertThat(firstCertificate.getId()).isNotEqualTo(secondCertificate.getId());
        assertThat(firstCertificate.getTraining().getId()).isNotEqualTo(secondCertificate.getTraining().getId());
    }

    @Test
    void testFindCertificateByIdSuccess() {
        Certificate savedCertificate = certificateService.save(certificateDTO);

        Certificate foundCertificate = certificateService.findById(savedCertificate.getId());

        assertThat(foundCertificate).isNotNull();
        assertThat(foundCertificate.getId()).isEqualTo(savedCertificate.getId());
        assertThat(foundCertificate.getValidityPeriod()).isEqualTo(2);
        assertThat(foundCertificate.getPrice()).isEqualTo(50.0);
        assertThat(foundCertificate.getTraining().getId()).isEqualTo(training.getId());
    }

    @Test
    void testFindCertificateByIdNotFoundThrows() {
        assertThrows(NotFoundException.class, () -> certificateService.findById(999));
    }

    @Test
    void testFindCertificateByIdWithZeroIdThrowsNotFoundException() {
        assertThrows(NotFoundException.class, 
                () -> certificateService.findById(0));
    }

    @Test
    void testFindCertificateByIdWithNegativeIdThrowsNotFoundException() {
        assertThrows(NotFoundException.class, 
                () -> certificateService.findById(-1));
    }

    @Test
    void testFindCertificatesByTrainingIdSuccess() {
        Certificate certificate1 = certificateService.save(certificateDTO);
        assertThat(certificate1.getTraining().getId()).isEqualTo(training.getId());
    }

    @Test
    void testFindCertificateByTrainingIdWhenNoCertificateExistsThrowsNotFoundException() {
        assertThrows(NotFoundException.class, 
                () -> certificateService.findByTrainingId(anotherTraining.getId()));
    }

    @Test
    void testFindCertificateByNonExistentTrainingIdThrowsNotFoundException() {
        assertThrows(NotFoundException.class, 
                () -> certificateService.findByTrainingId(999));
    }

    @Test
    void testUpdateCertificateSuccess() {
        Certificate savedCertificate = certificateService.save(certificateDTO);
        
        CertificateDTO updateDTO = new CertificateDTO(
                training.getId(),
                5,
                100.0
        );
        
        Certificate updatedCertificate = certificateService.update(savedCertificate.getId(), updateDTO);
        
        assertThat(updatedCertificate).isNotNull();
        assertThat(updatedCertificate.getId()).isEqualTo(savedCertificate.getId());
        assertThat(updatedCertificate.getValidityPeriod()).isEqualTo(5);
        assertThat(updatedCertificate.getPrice()).isEqualTo(100.0);
        // Training should remain unchanged
        assertThat(updatedCertificate.getTraining().getId()).isEqualTo(training.getId());
    }

    @Test
    void testUpdateCertificateWithZeroValues() {
        Certificate savedCertificate = certificateService.save(certificateDTO);
        
        CertificateDTO updateDTO = new CertificateDTO(
                999,
                0,
                0.0
        );
        
        Certificate updatedCertificate = certificateService.update(savedCertificate.getId(), updateDTO);
        
        assertThat(updatedCertificate.getValidityPeriod()).isEqualTo(0);
        assertThat(updatedCertificate.getPrice()).isEqualTo(0.0);
    }

    @Test
    void testUpdateCertificateNotFoundThrowsNotFoundException() {
        CertificateDTO updateDTO = new CertificateDTO(
                training.getId(),
                5,
                100.0
        );
        
        assertThrows(NotFoundException.class, 
                () -> certificateService.update(999, updateDTO));
    }

    @Test
    void testUpdateCertificateWithNegativeIdThrowsNotFoundException() {
        CertificateDTO updateDTO = new CertificateDTO(
                training.getId(),
                5,
                100.0
        );
        
        assertThrows(NotFoundException.class, 
                () -> certificateService.update(-1, updateDTO));
    }

    @Test
    void testGetAllCertificatesWhenEmpty() {
        List<Certificate> certificates = certificateService.getAllCertificates();
        
        assertThat(certificates).isNotNull();
        assertThat(certificates).isEmpty();
    }

    @Test
    void testGetAllCertificatesWithOneCertificate() {
        Certificate savedCertificate = certificateService.save(certificateDTO);
        
        List<Certificate> certificates = certificateService.getAllCertificates();
        
        assertThat(certificates).hasSize(1);
        assertThat(certificates.get(0).getId()).isEqualTo(savedCertificate.getId());
    }

    @Test
    void testGetAllCertificatesWithMultipleCertificates() {
        // Save first certificate
        Certificate firstCertificate = certificateService.save(certificateDTO);
        
        // Save second certificate for different training
        CertificateDTO secondCertificateDTO = new CertificateDTO(
                anotherTraining.getId(),
                3,
                75.0
        );
        Certificate secondCertificate = certificateService.save(secondCertificateDTO);
        
        List<Certificate> certificates = certificateService.getAllCertificates();
        
        assertThat(certificates).hasSize(2);
        List<Integer> certificateIds = certificates.stream()
                .map(Certificate::getId)
                .collect(Collectors.toList());
        assertThat(certificateIds).containsExactlyInAnyOrder(
                firstCertificate.getId(), 
                secondCertificate.getId()
        );
    }

    @Test
    void testCertificateDataIntegrityAfterSave() {
        Certificate savedCertificate = certificateService.save(certificateDTO);
        
        // Verify data integrity by fetching from repository directly
        Optional<Certificate> repositoryCertificate = certificateRepository.findById(savedCertificate.getId());
        
        assertThat(repositoryCertificate).isPresent();
        assertThat(repositoryCertificate.get().getValidityPeriod()).isEqualTo(2);
        assertThat(repositoryCertificate.get().getPrice()).isEqualTo(50.0);
        assertThat(repositoryCertificate.get().getTraining().getId()).isEqualTo(training.getId());
    }

    @Test
    void testCertificateWithExtremeValues() {
        CertificateDTO extremeDTO = new CertificateDTO(
                training.getId(),
                Integer.MAX_VALUE,
                Double.MAX_VALUE
        );
        
        Certificate savedCertificate = certificateService.save(extremeDTO);
        
        assertThat(savedCertificate.getValidityPeriod()).isEqualTo(Integer.MAX_VALUE);
        assertThat(savedCertificate.getPrice()).isEqualTo(Double.MAX_VALUE);
    }
}
