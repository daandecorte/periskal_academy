package ap.student.project.backend.controller;

import ap.student.project.backend.authentication.crypto;
import ap.student.project.backend.dto.LoginRequest;
import ap.student.project.backend.dto.UserDTO;
import ap.student.project.backend.entity.Language;
import ap.student.project.backend.service.UserService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.spec.KeySpec;
import java.util.Base64;

/**
 * Controller responsible for handling authentication-related endpoints.
 * This controller provides authentication functionality using either username/password
 * credentials or a hardware dongle code. Upon successful authentication, user information
 * is retrieved from an external authentication service.
 */
@RestController
public class LoginController {
    public final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(LoginController.class);

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Processes login requests using either credentials (username/password) or a dongle code.
     * The method determines which authentication path to take based on the provided parameters.
     *
     * @param loginRequest the request containing login information (username, password, language, or dongle code)
     * @return a JSON string containing the authentication result or an error message
     * @throws IOException if an error occurs during communication with the authentication service
     */
    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public String login(@RequestBody LoginRequest loginRequest) throws IOException {
        String username = loginRequest.username().orElse(null);
        String password = loginRequest.password().orElse(null);
        String language = loginRequest.language().orElse(null);
        String login = loginRequest.login().orElse(null);

        if (login != null && !login.isEmpty()) {
            return authenticateWithDongle(login, language);
        }

        if (username != null && password != null) {
            return authenticateWithCredentials(username, password, language);
        }

        return "{\"text\": \"Missing authentication parameters \"}";
    }

    /**
     * Authenticates a user with username and password credentials.
     *
     * @param username the user's username
     * @param password the user's password
     * @param language the user's preferred language
     * @return a JSON string containing the authentication result or an error message
     * @throws IOException if an error occurs during communication with the authentication service
     */
    private String authenticateWithCredentials(String username, String password, String language) throws IOException {
        URL url = new URL("http://academyws.periskal.com/Academy.asmx");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        String encoded = encode(username, password);

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/soap+xml; charset=UTF-8");
        conn.setDoOutput(true);

        String soapRequest = makeCredentialsRequest(encoded);

        OutputStream os = conn.getOutputStream();
        os.write(soapRequest.getBytes());
        os.flush();
        os.close();

        int responseCode = conn.getResponseCode();
        if (responseCode == 200) {
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            br.close();

            try {
                JSONObject json = new JSONObject(XMLtoJSON(response.toString(), "AuthenticateResult"));
                JSONObject authenticateResult = json
                        .getJSONObject("Body")
                        .getJSONObject("AuthenticateResponse")
                        .getJSONObject("AuthenticateResult");
                String persikalId = authenticateResult.getString("ID");
                String firstname = authenticateResult.getString("Firstname");
                String lastname = authenticateResult.getString("Lastname");
                String shipname = authenticateResult.optString("Shipname", "");
                addUser(persikalId, firstname, lastname, shipname, language);
                return json.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return "{\"text\": \"User not found\"}" + e.getMessage();
            }

        } else {
            logger.error("HTTP Error: " + responseCode);
            return "{\"text\": \"Authentication service error: " + responseCode + "\"}";
        }
    }

    /**
     * Authenticates a user with a hardware dongle code.
     *
     * @param dongleCode the encrypted dongle code
     * @param language   the user's preferred language
     * @return a JSON string containing the authentication result or an error message
     * @throws IOException if an error occurs during communication with the authentication service
     */
    protected String authenticateWithDongle(String dongleCode, String language) throws IOException {
        // Process dongle code if it has DEBUG prefix
        String processedDongleCode;
        if (dongleCode.startsWith("DEBUG:")) {
            // For DEBUG mode, encrypt the plain dongle number provided after "DEBUG:"
            String plainDongleNumber = dongleCode.substring(6);
            try {
                int dongleNumber = Integer.parseInt(plainDongleNumber);
                processedDongleCode = crypto.encode(dongleNumber, null);
            } catch (Exception e) {
                logger.error("Error encrypting debug dongle code: " + e.getMessage());
                return "{\"text\": \"Invalid dongle number format\"}";
            }
        } else {
            // Normal mode: use the encrypted dongle code as is
            processedDongleCode = dongleCode;
        }

        URL url = new URL("http://academyws.periskal.com/Academy.asmx");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/soap+xml; charset=UTF-8");
        conn.setDoOutput(true);

        String soapRequest = makeDongleRequest(processedDongleCode);

        OutputStream os = conn.getOutputStream();
        os.write(soapRequest.getBytes());
        os.flush();
        os.close();

        int responseCode = conn.getResponseCode();
        if (responseCode == 200) { // = OK
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            br.close();

            try {
                JSONObject json = new JSONObject(XMLtoJSON(response.toString(), "Authenticate_DongleResult"));
                JSONObject authenticateResult = json
                        .getJSONObject("Body")
                        .getJSONObject("Authenticate_DongleResponse")
                        .getJSONObject("Authenticate_DongleResult");
                String persikalId = authenticateResult.getString("ID");
                String firstname = authenticateResult.getString("Firstname");
                String lastname = authenticateResult.getString("Lastname");
                String shipname = authenticateResult.optString("Shipname", "");
                addUser(persikalId, firstname, lastname, shipname, language);
                return json.toString();
            } catch (Exception e) {
                logger.error("Error processing dongle authentication response: " + e.getMessage());
                return "{\"text\": \"Invalid dongle or processing error\"}";
            }
        } else {
            logger.error("HTTP Error: " + responseCode);
            return "{\"text\": \"Authentication service error: " + responseCode + "\"}";
        }
    }

    /**
     * Converts XML response from the authentication service to JSON format.
     *
     * @param xml            the XML string to convert
     * @param resultNodeName the name of the node containing the authentication result
     * @return a JSON string representation of the XML response
     * @throws Exception if the XML cannot be parsed or the result node is not found
     */
    private String XMLtoJSON(String xml, String resultNodeName) throws Exception {
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        JsonNode node = xmlMapper.readTree(xml.getBytes());

        JsonNode authResultNode = findNode(node, resultNodeName);
        if (authResultNode == null || authResultNode.isMissingNode()) {
            throw new Exception(resultNodeName + " not found in XML.");
        }

        if (authResultNode.has("Password")) {
            ((ObjectNode) authResultNode).remove("Password");
        }

        if (authResultNode.has("Username")) {
            ((ObjectNode) authResultNode).remove("Username");
        }

        JsonNode skippersNode = authResultNode.get("Skippers");
        if (skippersNode != null && skippersNode.has("Client")) {
            for (JsonNode client : skippersNode.get("Client")) {
                if (client.has("Password")) {
                    ((ObjectNode) client).remove("Password");
                }
            }
        }

        ObjectMapper jsonMapper = new ObjectMapper();
        return jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
    }

    private JsonNode findNode(JsonNode root, String nodeName) {
        if (root.has(nodeName)) {
            return root.get(nodeName);
        }
        for (JsonNode child : root) {
            JsonNode result = findNode(child, nodeName);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    /**
     * Creates a SOAP request for username/password authentication.
     *
     * @param encoded the Base64-encoded and encrypted username:password string
     * @return a SOAP request XML string
     */
    private String makeCredentialsRequest(String encoded) {
        return "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
                + "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
                + "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" "
                + "xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">"
                + "<soap12:Body>"
                + "<Authenticate xmlns=\"http://tempuri.org/\">"
                + "<Token>" + encoded + "</Token>"
                + "</Authenticate>"
                + "</soap12:Body>"
                + "</soap12:Envelope>";
    }

    /**
     * Creates a SOAP request for dongle authentication.
     *
     * @param dongleCode the encoded dongle code
     * @return a SOAP request XML string
     */
    private String makeDongleRequest(String dongleCode) {
        return "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
                + "<soap12:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
                + "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" "
                + "xmlns:soap12=\"http://www.w3.org/2003/05/soap-envelope\">"
                + "<soap12:Body>"
                + "<Authenticate_Dongle xmlns=\"http://tempuri.org/\">"
                + "<Dongle>" + dongleCode + "</Dongle>"
                + "</Authenticate_Dongle>"
                + "</soap12:Body>"
                + "</soap12:Envelope>";
    }

    private final String salt = "PeriskalAcademy2024";

    /**
     * Encodes a username and password combination for authentication.
     *
     * @param username the username to encode
     * @param password the password to encode
     * @return a Base64-encoded encrypted string
     * @throws IllegalArgumentException if username or password is empty
     */
    private String encode(String username, String password) {
        if (username.trim().isEmpty() || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Username and/or password cannot be empty.");
        }

        String combined = username + ":" + password;

        String encrypted = encryptString(combined);

        return Base64.getEncoder().encodeToString(encrypted.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Encrypts a string using AES encryption with CBC mode and PKCS5 padding.
     *
     * @param input the string to encrypt
     * @return the Base64-encoded encrypted string or null if encryption fails
     */
    private String encryptString(String input) {
        try {
            byte[] key = deriveKeyFromSalt(salt);
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
            byte[] iv = new byte[16];
            IvParameterSpec ivSpec = new IvParameterSpec(iv);

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivSpec);

            byte[] encryptedBytes = cipher.doFinal(input.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {

        }
        return null;
    }

    /**
     * Derives a cryptographic key from a provided salt using PBKDF2 with HMAC-SHA1.
     *
     * @param salt the input salt string
     * @return a derived key as a byte array
     */
    private byte[] deriveKeyFromSalt(String salt) {
        try {
            byte[] saltBytes = salt.getBytes(StandardCharsets.UTF_8); // Ensure same UTF-8 encoding as C#

            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            KeySpec spec = new PBEKeySpec(salt.toCharArray(), saltBytes, 10000, 256);
            return factory.generateSecret(spec).getEncoded();
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * Adds a new user to the system if one does not already exist with the given Periskal ID.
     *
     * @param persikalId the unique Periskal ID of the user
     * @param firstname  the first name of the user
     * @param lastname   the last name of the user
     * @param shipname   the name of the ship the user is associated with
     * @param language   the preferred language as a string; must match a valid Language enum value
     * @throws IllegalArgumentException if the provided language string does not match any {@link Language} value
     */
    private void addUser(String persikalId, String firstname, String lastname, String shipname, String language) {
        if (!userService.existsByPeriskalId(persikalId)) {
            UserDTO userDTO = new UserDTO(persikalId, firstname, lastname, shipname, Language.valueOf(language));
            userService.save(userDTO);
        }
    }
}