package ap.student.project.backend.service;

import ap.student.project.backend.dao.UserRepository;
import ap.student.project.backend.dto.CertificateDTO;
import ap.student.project.backend.dto.TrainingDTO;
import ap.student.project.backend.dto.UserCertificateDTO;
import ap.student.project.backend.dto.UserDTO;
import ap.student.project.backend.entity.*;
import ap.student.project.backend.exceptions.DuplicateException;
import ap.student.project.backend.exceptions.MissingArgumentException;
import ap.student.project.backend.exceptions.NotFoundException;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
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
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    void setup() {
        userDTO = new UserDTO("a", "b", "c", "c", Language.ENGLISH);
        user = userService.save(userDTO);

        trainingDTO = new TrainingDTO(null, null, true, null, null);
        Training training = trainingService.save(trainingDTO);

        certificateDTO = new CertificateDTO(training.getId(), 1, 1);
        certificate = certificateService.save(certificateDTO);
        
        // Ensure the bidirectional relationship is established
        training.setCertificate(certificate);
        certificate.setTraining(training);
        trainingService.save(trainingDTO);
    }

    @Test
    void save_ShouldPersistUserCertificate() {
        UserCertificateDTO dto = new UserCertificateDTO(
                LocalDate.now(),
                LocalDate.now().plusYears(1),
                CertificateStatus.VALID,
                user.getId(),
                certificate.getId()
        );

        UserCertificate saved = userCertificateService.save(dto);

        assertThat(saved).isNotNull();
        assertThat(saved.getUser().getId()).isEqualTo(user.getId());
        assertThat(saved.getCertificate().getId()).isEqualTo(certificate.getId());
        assertThat(saved.getStatus()).isEqualTo(CertificateStatus.VALID);
    }

    @Test
    void save_ShouldThrowMissingArgumentException_WhenUserIdIsMissing() {
        UserCertificateDTO dto = new UserCertificateDTO(
                LocalDate.now(),
                LocalDate.now().plusYears(1),
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
                LocalDate.now(),
                LocalDate.now().plusYears(1),
                CertificateStatus.VALID,
                user.getId(),
                0
        );

        assertThatThrownBy(() -> userCertificateService.save(dto))
                .isInstanceOf(MissingArgumentException.class)
                .hasMessageContaining("certificate_id is missing");
    }

    @Test
    void save_ShouldThrowDuplicateException_WhenUserCertificateAlreadyExists() {
        UserCertificateDTO dto = new UserCertificateDTO(
                LocalDate.now(),
                LocalDate.now().plusYears(1),
                CertificateStatus.VALID,
                user.getId(),
                certificate.getId()
        );
        userCertificateService.save(dto);

        // Try to save the same combination again
        UserCertificateDTO duplicateDto = new UserCertificateDTO(
                LocalDate.now().plusDays(1), // Different dates but same user/certificate
                LocalDate.now().plusYears(2),
                CertificateStatus.EXPIRED,
                user.getId(),
                certificate.getId()
        );

        assertThatThrownBy(() -> userCertificateService.save(duplicateDto))
                .isInstanceOf(DuplicateException.class)
                .hasMessageContaining("user certificate already exists");
    }

    @Test
    void save_ShouldSetAllFieldsCorrectly() {
        LocalDate issueDate = LocalDate.of(2024, 1, 1);
        LocalDate expiryDate = LocalDate.of(2025, 1, 1);
        
        UserCertificateDTO dto = new UserCertificateDTO(
                issueDate,
                expiryDate,
                CertificateStatus.EXPIRED,
                user.getId(),
                certificate.getId()
        );

        UserCertificate saved = userCertificateService.save(dto);

        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isGreaterThan(0);
        assertThat(saved.getIssueDate()).isEqualTo(issueDate);
        assertThat(saved.getExpiryDate()).isEqualTo(expiryDate);
        assertThat(saved.getStatus()).isEqualTo(CertificateStatus.EXPIRED);
        assertThat(saved.getUser().getId()).isEqualTo(user.getId());
        assertThat(saved.getCertificate().getId()).isEqualTo(certificate.getId());
    }

    @Test
    void save_ShouldThrowNotFoundException_WhenUserDoesNotExist() {
        UserCertificateDTO dto = new UserCertificateDTO(
                LocalDate.now(),
                LocalDate.now().plusYears(1),
                CertificateStatus.VALID,
                9999,
                certificate.getId()
        );

        assertThatThrownBy(() -> userCertificateService.save(dto))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void save_ShouldThrowNotFoundException_WhenCertificateDoesNotExist() {
        UserCertificateDTO dto = new UserCertificateDTO(
                LocalDate.now(),
                LocalDate.now().plusYears(1),
                CertificateStatus.VALID,
                user.getId(),
                9999 // Non-existent certificate ID
        );

        assertThatThrownBy(() -> userCertificateService.save(dto))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void save_ShouldWorkWithDifferentCertificateStatuses() {
        CertificateStatus[] statuses = {CertificateStatus.VALID, CertificateStatus.EXPIRED, CertificateStatus.REVOKED};
        
        // Create additional certificates for testing different statuses
        TrainingDTO training2DTO = new TrainingDTO(null, null, true, null, null);
        Training training2 = trainingService.save(training2DTO);
        CertificateDTO certificate2DTO = new CertificateDTO(training2.getId(), 1, 1);
        Certificate certificate2 = certificateService.save(certificate2DTO);

        TrainingDTO training3DTO = new TrainingDTO(null, null, true, null, null);
        Training training3 = trainingService.save(training3DTO);
        CertificateDTO certificate3DTO = new CertificateDTO(training3.getId(), 1, 1);
        Certificate certificate3 = certificateService.save(certificate3DTO);

        Certificate[] certificates = {certificate, certificate2, certificate3};

        for (int i = 0; i < statuses.length; i++) {
            UserCertificateDTO dto = new UserCertificateDTO(
                    LocalDate.now(),
                    LocalDate.now().plusYears(1),
                    statuses[i],
                    user.getId(),
                    certificates[i].getId()
            );

            UserCertificate saved = userCertificateService.save(dto);
            
            assertThat(saved.getStatus()).isEqualTo(statuses[i]);
        }
    }

    @Test
    void findById_ShouldReturnSavedUserCertificate() {
        UserCertificateDTO dto = new UserCertificateDTO(
                LocalDate.now(),
                LocalDate.now().plusYears(1),
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
                LocalDate.now(),
                LocalDate.now().plusYears(1),
                CertificateStatus.VALID,
                user.getId(),
                certificate.getId()
        );
        userCertificateService.save(dto);

        List<UserCertificate> all = userCertificateService.findAll();

        assertThat(all).isNotEmpty();
        assertThat(all).hasSizeGreaterThanOrEqualTo(1);
    }

    @Test
    void findAll_ShouldReturnEmptyList_WhenNoUserCertificatesExist() {
        List<UserCertificate> all = userCertificateService.findAll();
        assertThat(all).isEmpty();
    }

    @Test
    void findAll_ShouldReturnAllUserCertificatesFromMultipleUsers() {
        // Create another user
        UserDTO anotherUserDTO = new UserDTO("d", "e", "f", "f", Language.ENGLISH);
        User anotherUser = userService.save(anotherUserDTO);

        // Create certificates for both users
        UserCertificateDTO dto1 = new UserCertificateDTO(
                LocalDate.now(),
                LocalDate.now().plusYears(1),
                CertificateStatus.VALID,
                user.getId(),
                certificate.getId()
        );

        UserCertificateDTO dto2 = new UserCertificateDTO(
                LocalDate.now(),
                LocalDate.now().plusYears(2),
                CertificateStatus.EXPIRED,
                anotherUser.getId(),
                certificate.getId()
        );

        UserCertificate saved1 = userCertificateService.save(dto1);
        UserCertificate saved2 = userCertificateService.save(dto2);

        List<UserCertificate> all = userCertificateService.findAll();

        assertThat(all).hasSize(2);
        assertThat(all)
                .extracting(UserCertificate::getId)
                .containsExactlyInAnyOrder(saved1.getId(), saved2.getId());
    }

    @Test
    void findByUserId_ShouldReturnCertificatesForUser() {
        user.setUserCertificates(new ArrayList<>());
        User savedUser = userRepository.save(user);

        UserCertificateDTO dto = new UserCertificateDTO(
                LocalDate.now(),
                LocalDate.now().plusYears(1),
                CertificateStatus.VALID,
                savedUser.getId(),
                certificate.getId()
        );

        userCertificateService.save(dto);

        entityManager.flush();
        entityManager.clear();

        List<UserCertificate> userCertificates = userCertificateService.findByUserId(savedUser.getId());

        assertThat(userCertificates).isNotEmpty();
    }

    @Test
    void findByUserId_ShouldThrowNotFoundException_WhenNoCertificates() {
        UserDTO anotherUserDTO = new UserDTO("d", "e", "f", "f", Language.ENGLISH);
        User anotherUser = userService.save(anotherUserDTO);
        anotherUser.setUserCertificates(new ArrayList<>());
        assertThatThrownBy(() -> userCertificateService.findByUserId(anotherUser.getId()))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("User with id " + anotherUser.getId() + " has no certificates");
    }

    @Test
    void findByUserId_ShouldReturnMultipleCertificatesForUser() {
        TrainingDTO secondTrainingDTO = new TrainingDTO(null, null, true, null, null);
        Training secondTraining = trainingService.save(secondTrainingDTO);
        CertificateDTO secondCertificateDTO = new CertificateDTO(secondTraining.getId(), 2, 2);
        Certificate secondCertificate = certificateService.save(secondCertificateDTO);

        // Create two user certificates
        UserCertificateDTO dto1 = new UserCertificateDTO(
                LocalDate.now(),
                LocalDate.now().plusYears(1),
                CertificateStatus.VALID,
                user.getId(),
                certificate.getId()
        );
        UserCertificateDTO dto2 = new UserCertificateDTO(
                LocalDate.now(),
                LocalDate.now().plusYears(2),
                CertificateStatus.EXPIRED,
                user.getId(),
                secondCertificate.getId()
        );

        userCertificateService.save(dto1);
        userCertificateService.save(dto2);

        entityManager.flush();
        entityManager.clear();

        List<UserCertificate> userCertificates = userCertificateService.findByUserId(user.getId());

        assertThat(userCertificates).hasSize(2);
        assertThat(userCertificates)
                .extracting(UserCertificate::getCertificate)
                .extracting(Certificate::getId)
                .containsExactlyInAnyOrder(certificate.getId(), secondCertificate.getId());
    }

    @Test
    void findByUserId_ShouldThrowNotFoundException_WhenUserDoesNotExist() {
        assertThatThrownBy(() -> userCertificateService.findByUserId(9999))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void findByTrainingIdAndUserId_ShouldReturnUserCertificate() {
        UserCertificateDTO dto = new UserCertificateDTO(
                LocalDate.now(),
                LocalDate.now().plusYears(1),
                CertificateStatus.REVOKED,
                user.getId(),
                certificate.getId()
        );
        UserCertificate saved = userCertificateService.save(dto);

        UserCertificate found = userCertificateService.findByTrainingIdAndUserId(
                certificate.getTraining().getId(), 
                user.getId()
        );

        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(saved.getId());
        assertThat(found.getStatus()).isEqualTo(CertificateStatus.REVOKED);
        assertThat(found.getCertificate().getTraining().getId()).isEqualTo(certificate.getTraining().getId());
    }

    @Test
    void findByTrainingIdAndUserId_ShouldThrowMissingArgumentException_WhenTrainingIdIsZero() {
        assertThatThrownBy(() -> userCertificateService.findByTrainingIdAndUserId(0, user.getId()))
                .isInstanceOf(MissingArgumentException.class)
                .hasMessageContaining("training_id is missing");
    }

    @Test
    void findByTrainingIdAndUserId_ShouldThrowMissingArgumentException_WhenUserIdIsZero() {
        assertThatThrownBy(() -> userCertificateService.findByTrainingIdAndUserId(certificate.getTraining().getId(), 0))
                .isInstanceOf(MissingArgumentException.class)
                .hasMessageContaining("user_id is missing");
    }

    @Test
    void findByTrainingIdAndUserId_ShouldThrowNotFoundException_WhenTrainingDoesNotExist() {
        assertThatThrownBy(() -> userCertificateService.findByTrainingIdAndUserId(9999, user.getId()))
                .isInstanceOf(NotFoundException.class);
    }

}
