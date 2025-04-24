package ap.student.project.backend.service;

import ap.student.project.backend.dao.UserRepository;
import ap.student.project.backend.dto.UserDTO;
import ap.student.project.backend.entity.Language;
import ap.student.project.backend.entity.User;
import ap.student.project.backend.exceptions.DuplicateException;
import ap.student.project.backend.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        userDTO = new UserDTO("testId", "John", "Doe", "Ship123", Language.ENGLISH);
        user = new User("testId", "John", "Doe", "Ship123", Language.ENGLISH);
    }

    @Test
    void save_ShouldThrowException_WhenUserAlreadyExists() {
        when(userRepository.existsByPeriskalId(user.getPeriskalId())).thenReturn(true);
        assertThrows(DuplicateException.class, () -> userService.save(userDTO));
    }

    @Test
    void save_ShouldSaveUser_WhenUserDoesNotExist() {
        when(userRepository.existsByPeriskalId(user.getPeriskalId())).thenReturn(false);
        userService.save(userDTO);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void findById_ShouldReturnUser_WhenUserExists() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        User foundUser = userService.findById(1);
        assertNotNull(foundUser);
        assertEquals("testId", foundUser.getPeriskalId());
    }

    @Test
    void findById_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> userService.findById(1));
    }
}
