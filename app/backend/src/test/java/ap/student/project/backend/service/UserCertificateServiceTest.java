package ap.student.project.backend.service;

import ap.student.project.backend.dto.CertificateDTO;
import ap.student.project.backend.dto.TrainingDTO;
import ap.student.project.backend.dto.UserCertificateDTO;
import ap.student.project.backend.dto.UserDTO;
import ap.student.project.backend.entity.*;
import ap.student.project.backend.exceptions.MissingArgumentException;
import ap.student.project.backend.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UserCertificateServiceTest {

    @Autowired
    private UserCertificateService userCertificateService;

    @Autowired
    private UserService userService;

    @Autowired
    private CertificateService certificateService;

    @Autowired
    private TrainingService trainingService;

    private UserDTO userDTO;
    private CertificateDTO certificateDTO;
    private User user;
    private Certificate certificate;
    private TrainingDTO trainingDTO;

    @BeforeEach
    void setup() {
        // Create and save User
        userDTO = new UserDTO("a", "b", "c", "c", Language.ENGLISH);
        user = userService.save(userDTO);

        // Create and save Training
        trainingDTO = new TrainingDTO(null, null, true, null, null);
        Training training = trainingService.save(trainingDTO);

        // Create and save Certificate
        certificateDTO = new CertificateDTO(training.getId(), 1, 1);
        certificate = certificateService.save(certificateDTO);
    }

    @Test
    void save_ShouldPersistUserCertificate() {
        // Arrange
        UserCertificateDTO dto = new UserCertificateDTO(
                LocalDateTime.now(),
                LocalDateTime.now().plusYears(1),
                CertificateStatus.VALID,
                user.getId(),
                certificate.getId()
        );

        // Act
        UserCertificate saved = userCertificateService.save(dto);

        // Assert
        assertThat(saved).isNotNull();
        assertThat(saved.getUser().getId()).isEqualTo(user.getId());
        assertThat(saved.getCertificate().getId()).isEqualTo(certificate.getId());
        assertThat(saved.getStatus()).isEqualTo(CertificateStatus.VALID);
    }

    @Test
    void save_ShouldThrowMissingArgumentException_WhenUserIdIsMissing() {
        UserCertificateDTO dto = new UserCertificateDTO(
                LocalDateTime.now(),
                LocalDateTime.now().plusYears(1),
                CertificateStatus.VALID,
                0,
                certificate.getId()
        );

        assertThatThrownBy(() -> userCertificateService.save(dto))
                .isInstanceOf(MissingArgumentException.class)
                .hasMessageContaining("user_id is missing");
    }

    @Test
    void save_ShouldThrowMissingArgumentException_WhenCertificateIdIsMissing() {
        UserCertificateDTO dto = new UserCertificateDTO(
                LocalDateTime.now(),
                LocalDateTime.now().plusYears(1),
                CertificateStatus.VALID,
                user.getId(),
                0
        );

        assertThatThrownBy(() -> userCertificateService.save(dto))
                .isInstanceOf(MissingArgumentException.class)
                .hasMessageContaining("certificate_id is missing");
    }

    @Test
    void findById_ShouldReturnSavedUserCertificate() {
        UserCertificateDTO dto = new UserCertificateDTO(
                LocalDateTime.now(),
                LocalDateTime.now().plusYears(1),
                CertificateStatus.VALID,
                user.getId(),
                certificate.getId()
        );
        UserCertificate saved = userCertificateService.save(dto);

        UserCertificate found = userCertificateService.findById(saved.getId());

        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(saved.getId());
    }

    @Test
    void findById_ShouldThrowNotFoundException_WhenUserCertificateDoesNotExist() {
        assertThatThrownBy(() -> userCertificateService.findById(9999))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("User Certificate with id 9999 not found");
    }

    @Test
    void findAll_ShouldReturnAllUserCertificates() {
        UserCertificateDTO dto = new UserCertificateDTO(
                LocalDateTime.now(),
                LocalDateTime.now().plusYears(1),
                CertificateStatus.VALID,
                user.getId(),
                certificate.getId()
        );
        userCertificateService.save(dto);
        userCertificateService.save(dto);

        List<UserCertificate> all = userCertificateService.findAll();

        assertThat(all).isNotEmpty();
        assertThat(all).hasSizeGreaterThanOrEqualTo(2);
    }

    @Test
    void findByUserId_ShouldReturnCertificatesForUser() {
        UserCertificateDTO dto = new UserCertificateDTO(
                LocalDateTime.now(),
                LocalDateTime.now().plusYears(1),
                CertificateStatus.VALID,
                user.getId(),
                certificate.getId()
        );
        userCertificateService.save(dto);

        List<UserCertificate> userCertificates = userCertificateService.findByUserId(user.getId());

        assertThat(userCertificates).isNotEmpty();
    }

    @Test
    void findByUserId_ShouldThrowNotFoundException_WhenNoCertificates() {
        // Create a second user with no certificates
        UserDTO anotherUserDTO = new UserDTO("d", "e", "f", "f", Language.ENGLISH);
        User anotherUser = userService.save(anotherUserDTO);

        assertThatThrownBy(() -> userCertificateService.findByUserId(anotherUser.getId()))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("User with id " + anotherUser.getId() + " has no certificates");
    }
}
