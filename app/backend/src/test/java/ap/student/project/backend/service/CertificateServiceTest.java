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
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@Transactional // if TrainingService needs mocks, adjust later
class CertificateServiceTest {

    @Autowired
    private CertificateService certificateService;

    @Autowired
    private TrainingService trainingService;

    @Autowired
    private CertificateRepository certificateRepository;

    private Training training;
    private TrainingDTO trainingDTO;
    private CertificateDTO certificateDTO;

    @BeforeEach
    void setUp() {
        trainingDTO = new TrainingDTO(null, null, true, null, null);
        training = trainingService.save(trainingDTO);
        certificateDTO = new CertificateDTO(
                training.getId(), // id (will be ignored)
                2,
                50
        );
    }

    @Test
    void testSaveCertificateSuccess() {
        Certificate savedCertificate = certificateService.save(certificateDTO);

        assertThat(savedCertificate.getId()).isNotZero();
        assertThat(savedCertificate.getValidityPeriod()).isEqualTo(2);
        assertThat(savedCertificate.getTraining().getId()).isEqualTo(training.getId());
    }

    @Test
    void testSaveCertificateMissingTrainingIdThrows() {
        certificateDTO=new CertificateDTO(0, 0, 0);
        assertThrows(MissingArgumentException.class, () -> certificateService.save(certificateDTO));
    }

    @Test
    void testFindCertificateByIdSuccess() {
        Certificate certificate = certificateService.save(certificateDTO);

        Certificate found = certificateService.findById(certificate.getId());

        assertThat(found).isNotNull();
        assertThat(found.getValidityPeriod()).isEqualTo(2);
    }

    @Test
    void testFindCertificateByIdNotFoundThrows() {
        assertThrows(NotFoundException.class, () -> certificateService.findById(999));
    }

    @Test
    void testFindCertificatesByTrainingIdSuccess() {
        Certificate certificate1 = certificateService.save(certificateDTO);
        assertThat(certificate1.getTraining().getId()).isEqualTo(training.getId());
    }

}
