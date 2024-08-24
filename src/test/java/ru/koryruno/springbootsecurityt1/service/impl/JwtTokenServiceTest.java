package ru.koryruno.springbootsecurityt1.service.impl;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import ru.koryruno.springbootsecurityt1.model.TokenData;
import ru.koryruno.springbootsecurityt1.service.JwtTokenService;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class JwtTokenServiceTest {

    @InjectMocks
    private JwtTokenService jwtTokenService;

    @Mock
    private Claims claims;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Устанавливаем значения для @Value полей через ReflectionTestUtils
        ReflectionTestUtils.setField(jwtTokenService, "jwtSecret", "test-secret");
        ReflectionTestUtils.setField(jwtTokenService, "tokenExpiration", Duration.ofMinutes(15));
        ReflectionTestUtils.setField(jwtTokenService, "refreshTokenExpiration", Duration.ofDays(30));
    }

    @Test
    void generateAuthTokenTest() {
        String username = "testUser";
        List<String> roles = Collections.singletonList("ROLE_USER");

        TokenData tokenData = jwtTokenService.generateAuthToken(username, roles);

        assertNotNull(tokenData.getToken());
        assertNotNull(tokenData.getRefreshToken());
    }

//    @Test
//    void refreshBaseTokenTest() {
//        String username = "testUser";
//        String refreshToken = "test-refresh-token";
//
//        // Настраиваем моки
//        Claims mockedClaims = mock(Claims.class);
//        when(mockedClaims.get("roles", List.class)).thenReturn(Collections.singletonList("ROLE_USER"));
//        when(Jwts.parser().setSigningKey("test-secret".getBytes()).parseClaimsJws(refreshToken).getBody()).thenReturn(mockedClaims);
//
//        TokenData tokenData = jwtTokenService.refreshBaseToken(username, refreshToken);
//
//        assertNotNull(tokenData.getToken());
//        assertEquals(refreshToken, tokenData.getRefreshToken());
//    }
//
//    @Test
//    void validateTokenTest() {
//        String token = "valid-token";
//
//        // Настраиваем моки
//        when(Jwts.parser().setSigningKey("test-secret".getBytes()).parseClaimsJws(token).getBody()).thenReturn(claims);
//
//        assertTrue(jwtTokenService.validateToken(token));
//    }
//
//    @Test
//    void validateTokenInvalidTest() {
//        String token = "invalid-token";
//
//        // Настраиваем моки
//        doThrow(new RuntimeException("Invalid token")).when(Jwts.parser()).setSigningKey("test-secret".getBytes()).parseClaimsJws(token);
//
//        assertFalse(jwtTokenService.validateToken(token));
//    }

}
