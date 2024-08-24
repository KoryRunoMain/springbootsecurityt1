package ru.koryruno.springbootsecurityt1.api.AuthApi;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.koryruno.springbootsecurityt1.exception.AuthException;
import ru.koryruno.springbootsecurityt1.model.requestDto.RefreshTokenRequest;
import ru.koryruno.springbootsecurityt1.model.requestDto.UserCredentialsRequest;
import ru.koryruno.springbootsecurityt1.model.responseDto.TokenResponse;
import ru.koryruno.springbootsecurityt1.service.TokenService;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class AuthControllerTest {

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private AuthController authController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    void signIn_Success() throws Exception {
        UserCredentialsRequest request = new UserCredentialsRequest("username", "password");
        TokenResponse tokenResponse = new TokenResponse("validAuthToken", "validRefreshToken");

        when(tokenService.signIn(request)).thenReturn(tokenResponse);

        mockMvc.perform(post("/api/v1/public/token/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"username\", \"password\": \"password\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string(("Success")))
                .andDo(print());
    }

    @Test
    void signIn_Failure_InvalidCredentials() throws Exception {
        UserCredentialsRequest request = new UserCredentialsRequest("username", "password");

        when(tokenService.signIn(request)).thenThrow(new AuthException("Authentication failed"));

        mockMvc.perform(post("/api/v1/public/token/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"username\", \"password\": \"password\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Authentication failed"))
                .andDo(print());
    }

    @Test
    void refresh_Success() throws Exception {
        RefreshTokenRequest request = new RefreshTokenRequest("validRefreshToken");
        TokenResponse tokenResponse = new TokenResponse("newAuthToken", "validRefreshToken");

        when(tokenService.refreshToken(request)).thenReturn(tokenResponse);

        mockMvc.perform(post("/api/v1/public/token/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"refreshToken\": \"validRefreshToken\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("newAuthToken"))
                .andExpect(jsonPath("$.refreshToken").value("validRefreshToken"))
                .andDo(print());
    }

}