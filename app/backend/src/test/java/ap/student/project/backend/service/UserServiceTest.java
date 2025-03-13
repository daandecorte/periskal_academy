package ap.student.project.backend.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ap.student.project.backend.dao.*;
import ap.student.project.backend.dto.UserDTO;
import ap.student.project.backend.entity.Language;
import ap.student.project.backend.entity.User;
import ap.student.project.backend.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserModuleRepository userModuleRepository;

    @Mock
    private UserExamRepository userExamRepository;

    @Mock
    private ExamRepository examRepository;

    @Mock
    private ModuleRepository moduleRepository;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        user = new User("1", Language.ENGLISH);
        userDTO = new UserDTO("1", Language.ENGLISH);
    }

    @Test
    void testSaveUser() {
        when(userRepository.save(any(User.class))).thenReturn(user);
        assertDoesNotThrow(() -> userService.save(userDTO));
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testUpdate_Success() {
        when(userRepository.findByUserId(userDTO.userId())).thenReturn(user);

        userService.update(userDTO);

        assertEquals(Language.ENGLISH, user.getLanguage());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testUpdate_UserNotFound() {
        when(userRepository.findByUserId(userDTO.userId())).thenReturn(null);

        assertThrows(NotFoundException.class, () -> userService.update(userDTO));
    }

    @Test
    void testFindById_Success() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        User foundUser = userService.findById(1);
        assertNotNull(foundUser);
        assertEquals("1", foundUser.getUserId());
    }

    @Test
    void testFindById_UserNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> userService.findById(1));
        assertEquals("User with id 1 not found", exception.getMessage());
    }

    @Test
    void testFindAllUsers() {
        List<User> users = Arrays.asList(user);
        when(userRepository.findAll()).thenReturn(users);
        List<User> foundUsers = userService.findAll();
        assertEquals(1, foundUsers.size());
        assertEquals(user, foundUsers.get(0));
    }

    @Test
    void testDeleteById() {
        assertDoesNotThrow(() -> userService.deleteById(1));
        verify(userRepository, times(1)).deleteById(1);
    }

    @Test
    void testGetAllUserModules_UserNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> userService.getAllUserModules(1));
    }

    @Test
    void testGetAllUserModules_Success() throws NotFoundException {
        user.setUserModules(new ArrayList<>());
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        assertDoesNotThrow(() -> userService.getAllUserModules(1));
    }

    @Test
    void testAddUserModule_UserNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> userService.addUserModule(1, 1));
    }

    @Test
    void testAddUserModule_ModuleNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(moduleRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> userService.addUserModule(1, 1));
    }
    @Test
    void testGetAllUserExams_UserNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> userService.getAllUserExams(1));
    }

    @Test
    void testGetAllUserExams_Success() throws NotFoundException {
        user.setUserExams(new ArrayList<>());
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        assertDoesNotThrow(() -> userService.getAllUserExams(1));
    }

    @Test
    void testAddUserExam_UserNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> userService.addUserExam(1, 1));
    }

    @Test
    void testAddUserExam_ExamNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(examRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> userService.addUserExam(1, 1));
    }
}
