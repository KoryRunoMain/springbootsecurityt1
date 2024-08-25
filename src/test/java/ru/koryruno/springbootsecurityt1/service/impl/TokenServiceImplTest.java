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

    // Init
    private final Long USER_ID = 1L;
    private final UserCredentialsRequest request = new UserCredentialsRequest("username", "password");
    private final UserCredentialsRequest wrongRequest = new UserCredentialsRequest("username", "wrongPassword");
    private final RefreshTokenRequest invalidTokenRequest = new RefreshTokenRequest("invalidToken");

    private final User user = User.builder()
            .username("username")
            .password("encodedPassword")
            .roles(List.of(new UserRole(USER_ID, "ROLE_USER")))
            .build();
    private final TokenData tokenData = new TokenData("validAuthToken", "validRefreshToken");

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void When_SignIn_Expect_Successfully() {
        when(userRepository.findByUsername(request.getUsername())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.getPassword(), user.getPassword())).thenReturn(true);

        when(jwtTokenService.generateAuthToken(user.getUsername(), List.of("ROLE_USER"))).thenReturn(tokenData);
        when(tokenMapper.toTokenResponse(tokenData)).thenReturn(new TokenResponse("validAuthToken", "validRefreshToken"));

        TokenResponse response = tokenService.signIn(request);

        assertNotNull(response);
        assertEquals("validAuthToken", response.getToken());
        assertEquals("validRefreshToken", response.getRefreshToken());
    }

    @Test
    public void When_RefreshToken_Expect_Successfully() {
        String refreshToken = "validRefreshToken";
        RefreshTokenRequest request = new RefreshTokenRequest(refreshToken);
        User user = new User();
        user.setUsername("username");

        when(jwtTokenService.validateToken(refreshToken)).thenReturn(true);
        when(jwtTokenService.getUsernameFromToken(refreshToken)).thenReturn("username");
        when(userRepository.findByUsername("username")).thenReturn(Optional.of(user));

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
    public void When_SignIn_With_InvalidCredentials_Expect_ThrowsException() {
        when(userRepository.findByUsername(wrongRequest.getUsername())).thenReturn(Optional.of(new User()));
        when(passwordEncoder.matches(wrongRequest.getPassword(), "userPassword")).thenReturn(false);

        assertThrows(AuthException.class, () -> tokenService.signIn(wrongRequest));
    }

    @Test
    public void When_RefreshToken_With_InvalidToken_Expect_ThrowsException() {
        when(jwtTokenService.validateToken(any())).thenReturn(false);

        assertThrows(AuthException.class, () -> tokenService.refreshToken(invalidTokenRequest));
    }

}
