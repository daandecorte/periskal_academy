package ap.student.project.backend.authentication;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Base64;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class crypto {
    private static final String[] DAY_KEYS = {
            "SundayKey123", "MondayKey456", "TuesdayKey789",
            "WednesdayKeyABC", "ThursdayKeyDEF", "FridayKeyGHI", "SaturdayKeyJKL"
    };

    public static String encode(int number, LocalDate customDate) throws Exception {
        if (number < 1 || number > 99999) {
            throw new IllegalArgumentException("Number must be between 1 and 99999.");
        }

        LocalDate currentDate = (customDate != null) ? customDate : LocalDate.now();
        int dayOfWeek = currentDate.getDayOfWeek().getValue() % 7; // Java's DayOfWeek starts at 1 (Monday)
        String dayKey = DAY_KEYS[dayOfWeek];

        String combined = String.format("%05d|%s", number, currentDate);
        byte[] combinedBytes = combined.getBytes(StandardCharsets.UTF_8);
        byte[] keyBytes = dayKey.getBytes(StandardCharsets.UTF_8);

        Mac hmac = Mac.getInstance("HmacSHA256");
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "HmacSHA256");
        hmac.init(keySpec);

        byte[] hashBytes = hmac.doFinal(combinedBytes);
        String encodedHash = Base64.getEncoder().encodeToString(hashBytes);

        return encodedHash.substring(0, 8) + ":" + (number % 100000);
    }

    public static int decode(String encodedString, LocalDate customDate) throws Exception {
        String[] parts = encodedString.split(":");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid encoded string format.");
        }

        int remainder;
        try {
            remainder = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid remainder value.");
        }

        LocalDate currentDate = (customDate != null) ? customDate : LocalDate.now();
        int dayOfWeek = currentDate.getDayOfWeek().getValue() % 7;
        String dayKey = DAY_KEYS[dayOfWeek];

        for (int i = 1; i <= 99999; i++) {
            if (i % 100000 == remainder) {
                String testEncode = encode(i, currentDate);
                if (testEncode.startsWith(parts[0])) {
                    return i;
                }
            }
        }

        throw new IllegalArgumentException("Unable to decode the number.");
    }
}
