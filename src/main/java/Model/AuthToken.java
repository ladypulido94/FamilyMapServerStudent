package Model;

import java.security.SecureRandom;
import java.util.Base64;

public class AuthToken {

    private String token;
    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder();

    public AuthToken() {
        this.token = generateNewToken();
    }

    public AuthToken(String token){this.token=token;}

    private static String generateNewToken() {
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
