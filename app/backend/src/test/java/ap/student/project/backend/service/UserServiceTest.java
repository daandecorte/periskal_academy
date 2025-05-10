package ap.student.project.backend.service;

import ap.student.project.backend.dao.UserRepository;
import ap.student.project.backend.dto.UserDTO;
import ap.student.project.backend.entity.Language;
import ap.student.project.backend.entity.User;
import ap.student.project.backend.exceptions.DuplicateException;
import ap.student.project.backend.exceptions.NotFoundException;
import jakarta.transaction.Transactional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UserServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        // Clean up database
        userRepository.deleteAll();
        
        // Create test data
        userDTO = new UserDTO("testId", "John", "Doe", "Ship123", Language.ENGLISH);
    }

    @AfterEach
    void tearDown() {
        // Clean up database
        userRepository.deleteAll();
    }

    @Test
    void save_ShouldThrowException_WhenUserAlreadyExists() {
        userService.save(userDTO);
        assertThrows(DuplicateException.class, () -> userService.save(userDTO));
    }

    @Test
    void save_ShouldSaveUser_WhenUserDoesNotExist() {
        User savedUser = userService.save(userDTO);
        
        assertNotNull(savedUser);
        assertNotNull(savedUser.getId());
        assertEquals("testId", savedUser.getPeriskalId());
        assertEquals("John", savedUser.getFirstname());
        assertEquals("Doe", savedUser.getLastname());
        assertEquals("Ship123", savedUser.getShipname());
        assertEquals(Language.ENGLISH, savedUser.getLanguage());
    }

    @Test
    void findById_ShouldReturnUser_WhenUserExists() {
        User savedUser = userService.save(userDTO);
        User foundUser = userService.findById(savedUser.getId());
        
        assertNotNull(foundUser);
        assertEquals(savedUser.getId(), foundUser.getId());
        assertEquals("testId", foundUser.getPeriskalId());
    }

    @Test
    void findById_ShouldThrowException_WhenUserNotFound() {
        assertThrows(NotFoundException.class, () -> userService.findById(9999));
    }
}
