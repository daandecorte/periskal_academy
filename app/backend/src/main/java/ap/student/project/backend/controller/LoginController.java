package ap.student.project.backend.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.spec.KeySpec;
import java.util.Base64;

@RestController
public class LoginController {
    @GetMapping(value="/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public String login(@RequestParam String username, @RequestParam String password, @RequestParam String language) throws IOException {
        URL url = new URL("http://academyws.periskal.com/Academy.asmx");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        String encoded = encode(username, password);

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/soap+xml; charset=UTF-8");
        conn.setDoOutput(true);

        String soapRequest = makeRequest(encoded);

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

            try{
                return XMLtoJSON(response.toString());
            } catch (Exception e){
                return "{\"text\": \"User not found\"}";
            }

        } else {
            System.out.println("HTTP Error: " + responseCode);
            return null;
        }
    }

    private String XMLtoJSON(String xml) throws Exception {
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        JsonNode node = xmlMapper.readTree(xml.getBytes());

        JsonNode authResultNode = findNode(node, "AuthenticateResult");
        if (authResultNode == null || authResultNode.isMissingNode()) {
            throw new Exception("AuthenticateResult not found in XML.");
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

    private String makeRequest(String encoded){
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

    private final String salt = "PeriskalAcademy2024";

    private String encode(String username, String password){
        if(username.trim().isEmpty() || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Username and/or password cannot be empty.");
        }

        String combined = username+":"+password;

        String encrypted = encryptString(combined);

        return Base64.getEncoder().encodeToString(encrypted.getBytes(StandardCharsets.UTF_8));
    }

    private String encryptString(String input){
        try{
            byte[] key = deriveKeyFromSalt(salt);
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
            byte[] iv = new byte[16];
            IvParameterSpec ivSpec = new IvParameterSpec(iv);

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivSpec);

            byte[] encryptedBytes = cipher.doFinal(input.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e){

        }
        return null;
    }

    private byte[] deriveKeyFromSalt(String salt){
        try {
            byte[] saltBytes = salt.getBytes(StandardCharsets.UTF_8); // Ensure same UTF-8 encoding as C#

            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            KeySpec spec = new PBEKeySpec(salt.toCharArray(), saltBytes, 10000, 256);
            return factory.generateSecret(spec).getEncoded();
        } catch (Exception e) {}
        return null;
    }
}
