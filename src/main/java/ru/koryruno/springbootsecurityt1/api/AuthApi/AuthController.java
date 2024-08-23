package ru.koryruno.springbootsecurityt1.api.AuthApi;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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
    public ResponseEntity<String> signIn(@RequestBody UserCredentialsRequest userCredentialsDto) {
        try {
            TokenResponse jwtAuthenticationDto = tokenService.signIn(userCredentialsDto);
            return ResponseEntity.ok("Success");
        } catch (AuthException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed");
        }
    }

    @PostMapping("/refresh")
    public TokenResponse refresh(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return tokenService.refreshToken(refreshTokenRequest);
    }

}
