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

    // Init
    private static final UserCredentialsRequest CREDENTIALS_REQUEST= new UserCredentialsRequest("username", "password");
    private static final TokenResponse TOKEN_RESPONSE = new TokenResponse("validAuthToken", "validRefreshToken");

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    public void When_SignIn_Expect_Successfully() throws Exception {
        when(tokenService.signIn(CREDENTIALS_REQUEST)).thenReturn(TOKEN_RESPONSE);

        mockMvc.perform(post("/api/v1/public/token/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"username\", \"password\": \"password\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string(("Success. authToken: validAuthToken refreshToken: validRefreshToken")))
                .andDo(print());
    }

    @Test
    public void When_SignIn_With_InvalidCredentials_Expect_AuthenticationFailed() throws Exception {
        when(tokenService.signIn(CREDENTIALS_REQUEST)).thenThrow(new AuthException("Authentication failed"));

        mockMvc.perform(post("/api/v1/public/token/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"username\", \"password\": \"password\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Authentication failed"))
                .andDo(print());
    }

    @Test
    public void When_RefreshToken_Expect_Successfully() throws Exception {
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
