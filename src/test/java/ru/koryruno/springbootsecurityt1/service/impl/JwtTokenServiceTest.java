package ru.koryruno.springbootsecurityt1.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.koryruno.springbootsecurityt1.model.TokenData;
import ru.koryruno.springbootsecurityt1.service.JwtTokenService;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class JwtTokenServiceTest {

    private JwtTokenService jwtTokenService;

    private final static String JWT_SECRET = "testSecret";

    private Duration tokenExpiration = Duration.ofMinutes(30);

    private Duration refreshTokenExpiration = Duration.ofDays(7);

    private final String username = "testUser";
    private final List<String> roles = List.of("ROLE_USER");


    @Test
    public void testGenerateAuthToken() {
        String username = "testUser";
        List<String> roles = List.of("ROLE_USER");

        TokenData tokenData = jwtTokenService.generateAuthToken(username, roles);

        assertNotNull(tokenData.getToken());
        assertNotNull(tokenData.getRefreshToken());

        // Проверяем, что токен действителен
        assertTrue(jwtTokenService.validateToken(tokenData.getToken()));
        assertTrue(jwtTokenService.validateToken(tokenData.getRefreshToken()));
    }

    @Test
    public void testGetUsernameFromToken() {
        TokenData tokenData = jwtTokenService.generateAuthToken(username, roles);
        String token = tokenData.getToken();

        String extractedUsername = jwtTokenService.getUsernameFromToken(token);

        assertEquals(username, extractedUsername);
    }
}

    @Test
    public void testRefreshBaseToken() {
        String username = "testUser";
        String refreshToken = jwtTokenService.generateRefreshToken(username);

        TokenData newTokenData = jwtTokenService.refreshBaseToken(username, refreshToken);

        assertNotNull(newTokenData.getToken());
        assertEquals(refreshToken, newTokenData.getRefreshToken());

        // Проверяем, что новый токен действителен
        assertTrue(jwtTokenService.validateToken(newTokenData.getToken()));
    }

    @Test
    public void testValidateToken_ValidToken() {
        String username = "testUser";
        List<String> roles = List.of("ROLE_USER");
        String token = jwtTokenService.generateJwtToken(username, roles);

        boolean isValid = jwtTokenService.validateToken(token);

        assertTrue(isValid);
    }

    @Test
    public void testValidateToken_InvalidToken() {
        String invalidToken = "invalidToken";

        boolean isValid = jwtTokenService.validateToken(invalidToken);

        assertFalse(isValid);
    }

}