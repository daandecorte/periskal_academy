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

@RestController
public class LoginController {
    public final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

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
            System.out.println("HTTP Error: " + responseCode);
            return "{\"text\": \"Authentication service error: " + responseCode + "\"}";
        }
    }

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
                System.out.println("Error encrypting debug dongle code: " + e.getMessage());
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
                System.out.println("Error processing dongle authentication response: " + e.getMessage());
                return "{\"text\": \"Invalid dongle or processing error\"}";
            }
        } else {
            System.out.println("HTTP Error: " + responseCode);
            return "{\"text\": \"Authentication service error: " + responseCode + "\"}";
        }
    }

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

    private String encode(String username, String password) {
        if (username.trim().isEmpty() || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Username and/or password cannot be empty.");
        }

        String combined = username + ":" + password;

        String encrypted = encryptString(combined);

        return Base64.getEncoder().encodeToString(encrypted.getBytes(StandardCharsets.UTF_8));
    }

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

    private void addUser(String persikalId, String firstname, String lastname, String shipname, String language) {
        if (!userService.existsByPeriskalId(persikalId)) {
            UserDTO userDTO = new UserDTO(persikalId, firstname, lastname, shipname, Language.valueOf(language));
            userService.save(userDTO);
        }
    }
}