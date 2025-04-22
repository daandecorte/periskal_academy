package ap.student.project.backend.service;

import ap.student.project.backend.dao.CertificateRepository;
import ap.student.project.backend.dao.TrainingRepository;
import ap.student.project.backend.dao.UserRepository;
import ap.student.project.backend.dto.CertificateDTO;
import ap.student.project.backend.entity.Certificate;
import ap.student.project.backend.entity.Training;
import ap.student.project.backend.entity.User;
import ap.student.project.backend.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class CertificateServiceTest {

    @Autowired
    private CertificateService certificateService;

    @Autowired
    private CertificateRepository certificateRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TrainingService trainingService;

    @Autowired
    private TrainingRepository trainingRepository;

    private User user;
    private Training training;
    private CertificateDTO certificateDTO;

    @BeforeEach
    void setUp() {
        certificateRepository.deleteAll();
        userRepository.deleteAll();
        trainingRepository.deleteAll();

        user = new User();
        user.setFirstname("John");
        user.setLastname("Doe");
        user.setShipname("shipname");
        user = userRepository.save(user);

        training = new Training();
        training.setActive(true);
        training = trainingRepository.save(training);

        certificateDTO = new CertificateDTO(
                training.getId(),
                user.getId(),
                2,
                50
        );
    }

    @Test
    void save_ShouldSaveCertificate_WhenValidDTOProvided() {
        Certificate savedCertificate = certificateService.save(certificateDTO);

        assertNotNull(savedCertificate.getId());
        assertEquals(user.getId(), savedCertificate.getUser().getId());
        assertEquals(training.getId(), savedCertificate.getTraining().getId());
        assertEquals(2, savedCertificate.getValidityPeriod());
        assertEquals(50, savedCertificate.getPrice());
    }

    @Test
    void findById_ShouldReturnCertificate_WhenCertificateExists() {
        Certificate savedCertificate = certificateService.save(certificateDTO);

        Certificate foundCertificate = certificateService.findById(savedCertificate.getId());

        assertNotNull(foundCertificate);
        assertEquals(savedCertificate.getId(), foundCertificate.getId());
        assertEquals(2, foundCertificate.getValidityPeriod());
    }

    @Test
    void findById_ShouldThrowException_WhenCertificateDoesNotExist() {
        assertThrows(NotFoundException.class, () -> certificateService.findById(999));
    }

    @Test
    void findByUserId_ShouldReturnCertificates_WhenUserHasCertificates() {
        certificateService.save(certificateDTO);

        List<Certificate> certificates = certificateService.findByUserId(user.getId());

        assertNotNull(certificates);
        assertEquals(1, certificates.size());
        assertEquals(user.getId(), certificates.get(0).getUser().getId());
    }

    @Test
    void findByUserId_ShouldThrowException_WhenUserHasNoCertificates() {
        assertThrows(NotFoundException.class, () -> certificateService.findByUserId(user.getId()));
    }

    @Test
    void findByTrainingId_ShouldReturnCertificates_WhenTrainingHasCertificates() {
        certificateService.save(certificateDTO);

        List<Certificate> certificates = certificateService.findByTrainingId(training.getId());

        assertNotNull(certificates);
        assertEquals(1, certificates.size());
        assertEquals(training.getId(), certificates.get(0).getTraining().getId());
    }

    @Test
    void findByTrainingId_ShouldThrowException_WhenTrainingHasNoCertificates() {
        assertThrows(NotFoundException.class, () -> certificateService.findByTrainingId(training.getId()));
    }

    @Test
    void getAllCertificates_ShouldReturnAllCertificates() {
        CertificateDTO anotherCertificateDTO = new CertificateDTO(
                training.getId(),
                user.getId(),
                3,
                100
        );

        certificateService.save(certificateDTO);
        certificateService.save(anotherCertificateDTO);

        List<Certificate> certificates = certificateService.getAllCertificates();

        assertNotNull(certificates);
        assertEquals(2, certificates.size());
    }
}
