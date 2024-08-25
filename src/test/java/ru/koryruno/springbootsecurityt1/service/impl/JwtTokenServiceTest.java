package ru.koryruno.springbootsecurityt1.service.impl;

import io.jsonwebtoken.JwtParser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import ru.koryruno.springbootsecurityt1.model.TokenData;
import ru.koryruno.springbootsecurityt1.service.JwtTokenService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureMockMvc
public class JwtTokenServiceTest {

    @Autowired
    private JwtTokenService jwtTokenService;


    private static final String USER_NAME = "user";
    private final List<String> USER_ROLE = List.of("ROLE_USER");

    @Test
    public void testGenerateAuthToken() {
        TokenData tokenData = jwtTokenService.generateAuthToken(USER_NAME, USER_ROLE);
        assertNotNull(tokenData.getToken());
        assertNotNull(tokenData.getRefreshToken());
    }

    @Test
    public void testTokenExpiration() throws InterruptedException {
        TokenData initialTokenData = jwtTokenService.generateAuthToken(USER_NAME, USER_ROLE);
        Thread.sleep(2000);
        TokenData refreshedTokenData = jwtTokenService.refreshBaseToken(USER_NAME, initialTokenData.getRefreshToken());

        assertTrue(jwtTokenService.validateToken(refreshedTokenData.getToken()));
        assertFalse(jwtTokenService.validateToken(initialTokenData.getToken()));
    }

}