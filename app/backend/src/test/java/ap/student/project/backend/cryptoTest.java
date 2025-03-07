package ap.student.project.backend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import ap.student.project.backend.authentication.crypto;

public class cryptoTest {
    @Test
    void testEncodeDecode() throws Exception {
        int testNumber = 1181; // Lasts 5 numbers of PER00818
        LocalDate testDate = LocalDate.of(2025, 2, 26); // Fixed date for reproducible testing

        // Encoding
        String encodedValue = crypto.encode(testNumber, testDate);
        assertNotNull(encodedValue, "Encoded value should not be null");
        assertTrue(encodedValue.contains(":"), "Encoded value should contain ':' separator");

        // Decoding
        int decodedNumber = crypto.decode(encodedValue, testDate);
        assertEquals(testNumber, decodedNumber, "Decoded number should match original");

        // Check the generated API value
        String dongleCode = String.format("PER%05d", decodedNumber);
        assertEquals("PER01181", dongleCode, "Generated API string should match expected format");
    }

    @Test
    void testInvalidNumberEncoding() {
        assertThrows(IllegalArgumentException.class, () -> crypto.encode(0, LocalDate.now()),
                "Encoding should fail for numbers < 1");
        assertThrows(IllegalArgumentException.class, () -> crypto.encode(100000, LocalDate.now()),
                "Encoding should fail for numbers > 99999");
    }

    @Test
    void testInvalidEncodedStringFormat() {
        assertThrows(IllegalArgumentException.class, () -> crypto.decode("invalid:format", LocalDate.now()),
                "Decoding should fail for incorrect format");
        assertThrows(IllegalArgumentException.class, () -> crypto.decode("12345678:abc", LocalDate.now()),
                "Decoding should fail if remainder is not a number");
    }
}
