package ru.koryruno.springbootsecurityt1.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import ru.koryruno.springbootsecurityt1.model.TokenData;
import ru.koryruno.springbootsecurityt1.service.JwtTokenService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureMockMvc
public class JwtTokenServiceTest {

    @Autowired
    private JwtTokenService jwtTokenService;

    @Test
    public void testGenerateAuthToken() {
        TokenData tokenData = jwtTokenService.generateAuthToken("user", List.of("ROLE_USER"));
        assertNotNull(tokenData.getToken());
        assertNotNull(tokenData.getRefreshToken());
    }

    @Test
    public void testTokenExpiration() throws InterruptedException {
        String token = jwtTokenService.generateJwtToken("user", List.of("ROLE_USER"));
        Thread.sleep(2000);
        assertFalse(jwtTokenService.validateToken(token));
    }
}