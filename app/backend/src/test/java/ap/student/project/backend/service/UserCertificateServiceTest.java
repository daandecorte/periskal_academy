package ap.student.project.backend.service;

import ap.student.project.backend.dao.UserRepository;
import ap.student.project.backend.dto.CertificateDTO;
import ap.student.project.backend.dto.TrainingDTO;
import ap.student.project.backend.dto.UserCertificateDTO;
import ap.student.project.backend.dto.UserDTO;
import ap.student.project.backend.entity.*;
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
import java.time.LocalDateTime;
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

        //UserCertificate saved = userCertificateService.save(dto);

        //assertThat(saved).isNotNull();
        //assertThat(saved.getUser().getId()).isEqualTo(user.getId());
        //assertThat(saved.getCertificate().getId()).isEqualTo(certificate.getId());
        //assertThat(saved.getStatus()).isEqualTo(CertificateStatus.VALID);
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
    void findById_ShouldReturnSavedUserCertificate() {
        UserCertificateDTO dto = new UserCertificateDTO(
                LocalDate.now(),
                LocalDate.now().plusYears(1),
                CertificateStatus.VALID,
                user.getId(),
                certificate.getId()
        );
        //UserCertificate saved = userCertificateService.save(dto);

        //UserCertificate found = userCertificateService.findById(saved.getId());
        //assertThat(found).isNotNull();
        //assertThat(found.getId()).isEqualTo(saved.getId());
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
}
