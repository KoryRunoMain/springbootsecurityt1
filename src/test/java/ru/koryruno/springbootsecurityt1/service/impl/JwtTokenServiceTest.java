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
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureMockMvc
public class JwtTokenServiceTest {

    @Autowired
    private JwtTokenService jwtTokenService;

    // Init
    private static final String USER_NAME = "user";
    private static final List<String> USER_ROLE = List.of("ROLE_USER");

    // Sleep time is 2000 millis cause application.properties has jwt.token.expired=1s
    private static final int WAIT_TIME_MILLIS = 2000;

    @Test
    public void When_GenerateAuthToken_Expect_Successfully() {
        TokenData tokenData = jwtTokenService.generateAuthToken(USER_NAME, USER_ROLE);
        assertNotNull(tokenData.getToken());
        assertNotNull(tokenData.getRefreshToken());
    }

    @Test
    public void When_TokenExpiration_Expect_Successfully() throws InterruptedException {
        TokenData initialTokenData = jwtTokenService.generateAuthToken(USER_NAME, USER_ROLE);

        Thread.sleep(WAIT_TIME_MILLIS);

        TokenData refreshedTokenData = jwtTokenService.refreshBaseToken(USER_NAME, initialTokenData.getRefreshToken());

        assertTrue(jwtTokenService.validateToken(refreshedTokenData.getToken()));
        assertFalse(jwtTokenService.validateToken(initialTokenData.getToken()));
    }

}
