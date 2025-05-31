package ap.student.project.backend.service;

import ap.student.project.backend.dao.UserRepository;
import ap.student.project.backend.dto.UserDTO;
import ap.student.project.backend.entity.Language;
import ap.student.project.backend.entity.User;
import ap.student.project.backend.entity.UserCertificate;
import ap.student.project.backend.entity.UserTraining;
import ap.student.project.backend.exceptions.DuplicateException;
import ap.student.project.backend.exceptions.NotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UserServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    private UserDTO userDTO;
    private UserDTO anotherUserDTO;

    @BeforeEach
    void setUp() {
        // Clean up database
        userRepository.deleteAll();

        // Create test data
        userDTO = new UserDTO("testId", "John", "Doe", "Ship123", Language.ENGLISH);
        anotherUserDTO = new UserDTO("anotherId", "Jane", "Smith", "Ship456", Language.DUTCH);
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
    void save_ShouldSaveMultipleUsers_WhenTheyHaveDifferentPeriskalIds() {
        User firstUser = userService.save(userDTO);
        User secondUser = userService.save(anotherUserDTO);

        assertNotNull(firstUser);
        assertNotNull(secondUser);
        assertNotEquals(firstUser.getId(), secondUser.getId());
        assertEquals("testId", firstUser.getPeriskalId());
        assertEquals("anotherId", secondUser.getPeriskalId());
    }

    @Test
    void update_ShouldUpdateUserLanguage_WhenUserExists() {
        User savedUser = userService.save(userDTO);
        UserDTO updateDTO = new UserDTO("testId", "John", "Doe", "Ship123", Language.DUTCH);

        userService.update(updateDTO);

        User updatedUser = userRepository.findById(savedUser.getId()).orElse(null);
        assertNotNull(updatedUser);
        assertEquals(Language.DUTCH, updatedUser.getLanguage());
        // Verify other fields remain unchanged
        assertEquals("John", updatedUser.getFirstname());
        assertEquals("Doe", updatedUser.getLastname());
        assertEquals("Ship123", updatedUser.getShipname());
    }

    @Test
    void update_ShouldThrowException_WhenUserNotFound() {
        UserDTO nonExistentUserDTO = new UserDTO("nonExistent", "Test", "User", "Ship999", Language.ENGLISH);
        
        assertThrows(NotFoundException.class, () -> userService.update(nonExistentUserDTO));
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

    @Test
    void findByPeriskalId_ShouldReturnUser_WhenUserExists() {
        User savedUser = userService.save(userDTO);
        User foundUser = userService.findByPeriskalId("testId");

        assertNotNull(foundUser);
        assertEquals(savedUser.getId(), foundUser.getId());
        assertEquals("testId", foundUser.getPeriskalId());
        assertEquals("John", foundUser.getFirstname());
    }

    @Test
    void findByPeriskalId_ShouldThrowException_WhenUserNotFound() {
        assertThrows(NotFoundException.class, () -> userService.findByPeriskalId("nonExistent"));
    }

    @Test
    void existsByPeriskalId_ShouldReturnTrue_WhenUserExists() {
        userService.save(userDTO);
        
        boolean exists = userService.existsByPeriskalId("testId");
        
        assertTrue(exists);
    }

    @Test
    void existsByPeriskalId_ShouldReturnFalse_WhenUserDoesNotExist() {
        boolean exists = userService.existsByPeriskalId("nonExistent");
        
        assertFalse(exists);
    }

    @Test
    void findAll_ShouldReturnEmptyList_WhenNoUsersExist() {
        List<User> users = userService.findAll();
        
        assertNotNull(users);
        assertTrue(users.isEmpty());
    }

    @Test
    void findAll_ShouldReturnAllUsers_WhenUsersExist() {
        userService.save(userDTO);
        userService.save(anotherUserDTO);
        
        List<User> users = userService.findAll();
        
        assertNotNull(users);
        assertEquals(2, users.size());
        
        List<String> periskalIds = users.stream()
                .map(User::getPeriskalId)
                .collect(Collectors.toList());
        assertTrue(periskalIds.contains("testId"));
        assertTrue(periskalIds.contains("anotherId"));
    }

    @Test
    void getAllUserModules_ShouldThrowException_WhenUserNotFound() {
        assertThrows(NotFoundException.class, () -> userService.getAllUserModules(9999));
    }

    @Test
    void getAllUserModules_ShouldReturnEmptyList_WhenUserHasNoTrainings() {
        User savedUser = userService.save(userDTO);        
        List<UserTraining> result = userService.getAllUserModules(savedUser.getId());

        assertTrue(result.isEmpty());
    }

    @Test
    void getAllUserCertificates_ShouldThrowException_WhenUserNotFound() {
        assertThrows(NotFoundException.class, () -> userService.getAllUserCertificates(9999));
    }

    @Test
    void getAllUserCertificates_ShouldThrowException_WhenUserHasNoCertificates() {
        User savedUser = userService.save(userDTO);
        List<UserCertificate> result = userService.getAllUserCertificates(savedUser.getId());
        
        assertTrue(result.isEmpty());
    }

    @Test
    void getAllExamAttempts_ShouldThrowException_WhenUserNotFound() {
        assertThrows(NotFoundException.class, () -> userService.getAllExamAttempts(9999));
    }

    @Test
    void getAllExamAttempts_ShouldThrowException_WhenUserHasNoTrainings() {
        User savedUser = userService.save(userDTO);
        
        assertThrows(NotFoundException.class, () -> userService.getAllExamAttempts(savedUser.getId()));
    }

    @Test
    void getChatMember_ShouldThrowException_WhenUserNotFound() {
        assertThrows(NotFoundException.class, () -> userService.getChatMember(9999));
    }

    @Test
    void getChatMember_ShouldThrowException_WhenUserHasNoChatMembers() {
        User savedUser = userService.save(userDTO);
        
        assertThrows(NotFoundException.class, () -> userService.getChatMember(savedUser.getId()));
    }

    @Test
    void assemble_ShouldCreateUserFromDTO() {
        User assembledUser = userService.assemble(userDTO);
        
        assertNotNull(assembledUser);
        assertEquals("testId", assembledUser.getPeriskalId());
        assertEquals("John", assembledUser.getFirstname());
        assertEquals("Doe", assembledUser.getLastname());
        assertEquals("Ship123", assembledUser.getShipname());
        assertEquals(Language.ENGLISH, assembledUser.getLanguage());
        assertEquals(0, assembledUser.getId());
    }

    @Test
    void assemble_ShouldHandleDifferentLanguages() {
        UserDTO dutchUserDTO = new UserDTO("dutchId", "Jan", "Janssen", "ShipNL", Language.DUTCH);
        
        User assembledUser = userService.assemble(dutchUserDTO);
        
        assertEquals(Language.DUTCH, assembledUser.getLanguage());
        assertEquals("Jan", assembledUser.getFirstname());
        assertEquals("Janssen", assembledUser.getLastname());
    }

    @Test
    void integrationTest_CreateUpdateAndRetrieveUser() {
        // Create user
        User savedUser = userService.save(userDTO);
        assertNotNull(savedUser.getId());
        
        // Verify existence
        assertTrue(userService.existsByPeriskalId("testId"));
        
        // Update user
        UserDTO updateDTO = new UserDTO("testId", "John", "Doe", "Ship123", Language.DUTCH);
        userService.update(updateDTO);
        
        // Retrieve and verify update
        User updatedUser = userService.findByPeriskalId("testId");
        assertEquals(Language.DUTCH, updatedUser.getLanguage());
        
        // Verify by ID still works
        User foundById = userService.findById(savedUser.getId());
        assertEquals(Language.DUTCH, foundById.getLanguage());
    }

    @Test
    void integrationTest_MultipleUsersOperations() {
        // Create multiple users
        User user1 = userService.save(userDTO);
        User user2 = userService.save(anotherUserDTO);
        
        // Verify both exist
        List<User> allUsers = userService.findAll();
        assertEquals(2, allUsers.size());
        
        // Verify individual lookups
        User foundUser1 = userService.findByPeriskalId("testId");
        User foundUser2 = userService.findByPeriskalId("anotherId");
        
        assertEquals(user1.getId(), foundUser1.getId());
        assertEquals(user2.getId(), foundUser2.getId());
        
        // Update one user
        UserDTO updateDTO = new UserDTO("testId", "John", "Doe", "Ship123", Language.DUTCH);
        userService.update(updateDTO);
        
        // Verify only one user was updated
        User updatedUser1 = userService.findByPeriskalId("testId");
        User unchangedUser2 = userService.findByPeriskalId("anotherId");
        
        assertEquals(Language.DUTCH, updatedUser1.getLanguage());
        assertEquals(Language.DUTCH, unchangedUser2.getLanguage()); // Original language for user2
    }

    @Test
    void save_ShouldHandleNullValuesInDTO() {
        UserDTO dtoWithNulls = new UserDTO("nullTest", null, null, null, Language.ENGLISH);
        
        User savedUser = userService.save(dtoWithNulls);
        
        assertNotNull(savedUser);
        assertEquals("nullTest", savedUser.getPeriskalId());
        assertNull(savedUser.getFirstname());
        assertNull(savedUser.getLastname());
        assertNull(savedUser.getShipname());
        assertEquals(Language.ENGLISH, savedUser.getLanguage());
    }

    @Test
    void findAll_ShouldReturnUsersInConsistentOrder() {
        userService.save(userDTO);
        userService.save(anotherUserDTO);
        
        List<User> firstCall = userService.findAll();
        List<User> secondCall = userService.findAll();
        
        assertEquals(firstCall.size(), secondCall.size());
        for (int i = 0; i < firstCall.size(); i++) {
            assertEquals(firstCall.get(i).getId(), secondCall.get(i).getId());
        }
    }
}
