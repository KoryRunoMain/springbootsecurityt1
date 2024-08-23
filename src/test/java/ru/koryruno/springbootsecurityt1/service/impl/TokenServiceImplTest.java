package ru.koryruno.springbootsecurityt1.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.koryruno.springbootsecurityt1.exception.AuthException;
import ru.koryruno.springbootsecurityt1.model.TokenData;
import ru.koryruno.springbootsecurityt1.model.User;
import ru.koryruno.springbootsecurityt1.model.UserRole;
import ru.koryruno.springbootsecurityt1.model.mapper.TokenMapper;
import ru.koryruno.springbootsecurityt1.model.requestDto.RefreshTokenRequest;
import ru.koryruno.springbootsecurityt1.model.requestDto.UserCredentialsRequest;
import ru.koryruno.springbootsecurityt1.model.responseDto.TokenResponse;
import ru.koryruno.springbootsecurityt1.repository.UserRepository;
import ru.koryruno.springbootsecurityt1.service.JwtTokenService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class TokenServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private JwtTokenService jwtTokenService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private TokenMapper tokenMapper;

    @InjectMocks
    private TokenServiceImpl tokenService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void signIn_Success() {
        UserCredentialsRequest request = new UserCredentialsRequest("username", "password");
        User user = new User();
        user.setUsername("username");
        user.setPassword("encodedPassword");
        user.setRoles(List.of(new UserRole(1L, "ROLE_USER")));

        when(userRepository.findByUsername(request.getUsername())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.getPassword(), user.getPassword())).thenReturn(true);

        // Создаем объект TokenData
        TokenData tokenData = new TokenData();
        tokenData.setToken("validAuthToken");
        tokenData.setRefreshToken("validRefreshToken");

        // Мокаем методы
        when(jwtTokenService.generateAuthToken(user.getUsername(), List.of("ROLE_USER"))).thenReturn(tokenData);
        when(tokenMapper.toTokenResponse(tokenData)).thenReturn(new TokenResponse("validAuthToken", "validRefreshToken"));

        TokenResponse response = tokenService.signIn(request);

        assertNotNull(response);
        assertEquals("validAuthToken", response.getToken());
        assertEquals("validRefreshToken", response.getRefreshToken());
    }

    @Test
    void refreshToken_Success() {
        String refreshToken = "validRefreshToken";
        RefreshTokenRequest request = new RefreshTokenRequest(refreshToken);
        User user = new User();
        user.setUsername("username");

        when(jwtTokenService.validateToken(refreshToken)).thenReturn(true);
        when(jwtTokenService.getUsernameFromToken(refreshToken)).thenReturn("username");
        when(userRepository.findByUsername("username")).thenReturn(Optional.of(user));

        // Создаем новый объект TokenData для обновленного токена
        TokenData tokenData = new TokenData();
        tokenData.setToken("newAuthToken");
        tokenData.setRefreshToken(refreshToken);

        when(jwtTokenService.refreshBaseToken(user.getUsername(), refreshToken)).thenReturn(tokenData);
        when(tokenMapper.toTokenResponse(tokenData)).thenReturn(new TokenResponse("newAuthToken", refreshToken));

        TokenResponse response = tokenService.refreshToken(request);

        assertNotNull(response);
        assertEquals("newAuthToken", response.getToken());
        assertEquals(refreshToken, response.getRefreshToken());
    }

    @Test
    void signIn_Failure_InvalidCredentials() {
        UserCredentialsRequest request = new UserCredentialsRequest("username", "wrongPassword");
        when(userRepository.findByUsername(request.getUsername())).thenReturn(Optional.of(new User()));
        when(passwordEncoder.matches(request.getPassword(), "userPassword")).thenReturn(false);

        assertThrows(AuthException.class, () -> tokenService.signIn(request));
    }

    @Test
    void refreshToken_Failure_InvalidToken() {
        RefreshTokenRequest request = new RefreshTokenRequest("invalidToken");

        when(jwtTokenService.validateToken(any())).thenReturn(false);

        assertThrows(AuthException.class, () -> tokenService.refreshToken(request));
    }
}
