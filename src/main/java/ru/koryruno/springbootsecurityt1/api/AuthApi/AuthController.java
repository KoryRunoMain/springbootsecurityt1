package ru.koryruno.springbootsecurityt1.api.AuthApi;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.koryruno.springbootsecurityt1.exception.AuthException;
import ru.koryruno.springbootsecurityt1.model.requestDto.RefreshTokenRequest;
import ru.koryruno.springbootsecurityt1.model.responseDto.TokenResponse;
import ru.koryruno.springbootsecurityt1.model.requestDto.UserCredentialsRequest;
import ru.koryruno.springbootsecurityt1.service.TokenService;

@RestController
@RequestMapping(path = "/api/v1/public/token")
@RequiredArgsConstructor
public class AuthController {

    private final TokenService tokenService;

    @PostMapping("/password")
    public ResponseEntity<TokenResponse> signIn(@RequestBody UserCredentialsRequest userCredentialsDto) {
        try {
            TokenResponse jwtAuthenticationDto = tokenService.signIn(userCredentialsDto);
            return ResponseEntity.ok(jwtAuthenticationDto);
        } catch (AuthException e) {
            throw new AuthException("Authentication failed" + e.getMessage());
        }
    }

    @PostMapping("/refresh")
    public TokenResponse refresh(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return tokenService.refreshToken(refreshTokenRequest);
    }

}
