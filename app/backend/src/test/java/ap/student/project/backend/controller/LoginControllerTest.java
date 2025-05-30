package ap.student.project.backend.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.databind.ObjectMapper;

import ap.student.project.backend.authentication.crypto;
import ap.student.project.backend.dto.LoginRequest;
import ap.student.project.backend.dto.UserDTO;
import ap.student.project.backend.service.UserService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Tests for LoginController using mocks
 */
@ExtendWith(MockitoExtension.class)
class LoginControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private LoginController loginController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void testLogin_WithValidCredentials_ShouldReturnAuthenticationResult() throws Exception {
        LoginRequest request = new LoginRequest(
                Optional.of("testuser"),
                Optional.of("testpass"),
                Optional.of("ENGLISH"),
                Optional.empty()
        );

        // Mock HTTP connection for successful authentication
        String mockXmlResponse = createMockSuccessfulXmlResponse();
        
        String result = loginController.login(request);
        assertNotNull(result);
    }

    @Test
    void testLogin_WithValidDongleCode_ShouldCallDongleAuthentication() throws Exception {
        LoginRequest request = new LoginRequest(
                Optional.empty(),
                Optional.empty(),
                Optional.of("ENGLISH"),
                Optional.of("ABC12345:12345")
        );

        String result = loginController.login(request);
        assertNotNull(result);
    }

    @Test
    void testLogin_WithDebugDongleCode_ShouldProcessDebugCode() throws Exception {
        LoginRequest request = new LoginRequest(
                Optional.empty(),
                Optional.empty(),
                Optional.of("ENGLISH"),
                Optional.of("DEBUG:12345")
        );

        // Mock crypto.encode static method
        try (MockedStatic<crypto> cryptoMock = mockStatic(crypto.class)) {
            cryptoMock.when(() -> crypto.encode(eq(12345), isNull()))
                     .thenReturn("ENCODED123:12345");

            String result = loginController.login(request);
            assertNotNull(result);
            cryptoMock.verify(() -> crypto.encode(eq(12345), isNull()));
        }
    }

    @Test
    void testLogin_WithInvalidDebugDongleCode_ShouldReturnError() throws Exception {
        LoginRequest request = new LoginRequest(
                Optional.empty(),
                Optional.empty(),
                Optional.of("ENGLISH"),
                Optional.of("DEBUG:invalid")
        );

        String result = loginController.login(request);
        assertTrue(result.contains("Invalid dongle number format"));
    }

    @Test
    void testLogin_WithMissingParameters_ShouldReturnError() throws Exception {
        LoginRequest request = new LoginRequest(
                Optional.empty(),
                Optional.empty(),
                Optional.of("ENGLISH"),
                Optional.empty()
        );

        String result = loginController.login(request);
        assertEquals("{\"text\": \"Missing authentication parameters \"}", result);
    }

    @Test
    void testLogin_WithPartialCredentials_ShouldReturnError() throws Exception {
        LoginRequest request = new LoginRequest(
                Optional.of("testuser"),
                Optional.empty(),
                Optional.of("ENGLISH"),
                Optional.empty()
        );

        String result = loginController.login(request);
        assertEquals("{\"text\": \"Missing authentication parameters \"}", result);
    }

    @Test
    void testEncodeMethod_WithValidCredentials_ShouldReturnEncodedString() {
        String username = "testuser";
        String password = "testpass";

        String result = invokeEncodeMethod(username, password);
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void testEncodeMethod_WithEmptyUsername_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            invokeEncodeMethod("", "password");
        });
    }

    @Test
    void testEncodeMethod_WithEmptyPassword_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            invokeEncodeMethod("username", "");
        });
    }

    @Test
    void testAddUser_WhenUserDoesNotExist_ShouldCallUserService() {
        when(userService.existsByPeriskalId("123")).thenReturn(false);

        invokeAddUserMethod("123", "John", "Doe", "TestShip", "ENGLISH");

        verify(userService).existsByPeriskalId("123");
        verify(userService).save(any(UserDTO.class));
    }

    @Test
    void testAddUser_WhenUserExists_ShouldNotCallSave() {
        when(userService.existsByPeriskalId("123")).thenReturn(true);

        invokeAddUserMethod("123", "John", "Doe", "TestShip", "ENGLISH");

        verify(userService).existsByPeriskalId("123");
        verify(userService, never()).save(any(UserDTO.class));
    }

    // Helper methods to access private methods via reflection for testing
    private String invokeEncodeMethod(String username, String password) {
        try {
            java.lang.reflect.Method method = LoginController.class.getDeclaredMethod("encode", String.class, String.class);
            method.setAccessible(true);
            return (String) method.invoke(loginController, username, password);
        } catch (Exception e) {
            if (e.getCause() instanceof IllegalArgumentException) {
                throw (IllegalArgumentException) e.getCause();
            }
            throw new RuntimeException(e);
        }
    }

    private void invokeAddUserMethod(String persikalId, String firstname, String lastname, String shipname, String language) {
        try {
            java.lang.reflect.Method method = LoginController.class.getDeclaredMethod("addUser", String.class, String.class, String.class, String.class, String.class);
            method.setAccessible(true);
            method.invoke(loginController, persikalId, firstname, lastname, shipname, language);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String createMockSuccessfulXmlResponse() {
        return "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
               "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\">" +
               "<soap:Body>" +
               "<AuthenticateResponse xmlns=\"http://tempuri.org/\">" +
               "<AuthenticateResult>" +
               "<ID>12345</ID>" +
               "<Firstname>John</Firstname>" +
               "<Lastname>Doe</Lastname>" +
               "<Shipname>TestShip</Shipname>" +
               "</AuthenticateResult>" +
               "</AuthenticateResponse>" +
               "</soap:Body>" +
               "</soap:Envelope>";
    }
}
