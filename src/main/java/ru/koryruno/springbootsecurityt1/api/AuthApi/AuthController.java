package ru.koryruno.springbootsecurityt1.api.AuthApi;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.koryruno.springbootsecurityt1.exception.AuthException;
import ru.koryruno.springbootsecurityt1.model.requestDto.RefreshTokenRequest;
import ru.koryruno.springbootsecurityt1.model.responseDto.TokenResponse;
import ru.koryruno.springbootsecurityt1.model.requestDto.UserCredentialsRequest;
import ru.koryruno.springbootsecurityt1.service.TokenService;

@Slf4j
@RestController
@RequestMapping(path = "/api/v1/public/token")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthController {
    private final TokenService tokenService;

    @PostMapping("/password")
    public ResponseEntity<String> signIn(@RequestBody UserCredentialsRequest userCredentialsDto) {
        try {
            TokenResponse jwtAuthenticationDto = tokenService.signIn(userCredentialsDto);
            return ResponseEntity.ok("Success. authToken: " +
                    jwtAuthenticationDto.getToken() + " refreshToken: " +
                    jwtAuthenticationDto.getRefreshToken());
        } catch (AuthException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed");
        }
    }

    @PostMapping("/refresh")
    @ResponseStatus(HttpStatus.OK)
    public TokenResponse refresh(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return tokenService.refreshToken(refreshTokenRequest);
    }

}
