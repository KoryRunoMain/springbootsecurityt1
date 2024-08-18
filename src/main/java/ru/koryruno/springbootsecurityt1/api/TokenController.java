package ru.koryruno.springbootsecurityt1.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.koryruno.springbootsecurityt1.exception.AuthException;
import ru.koryruno.springbootsecurityt1.exception.RefreshTokenException;
import ru.koryruno.springbootsecurityt1.model.dto.PasswordTokenRequest;
import ru.koryruno.springbootsecurityt1.model.dto.RefreshTokenRequest;
import ru.koryruno.springbootsecurityt1.model.dto.TokenData;
import ru.koryruno.springbootsecurityt1.model.dto.TokenResponse;
import ru.koryruno.springbootsecurityt1.service.SecurityService;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/public/token")
public class TokenController {
    private final SecurityService securityService;

    @PostMapping(path = "/password")
    public ResponseEntity<TokenResponse> password(@RequestBody PasswordTokenRequest passwordTokenRequest) throws RefreshTokenException, AuthException {
        TokenData tokenData = securityService.processPasswordToken(passwordTokenRequest.getUsername(), passwordTokenRequest.getPassword());
        return ResponseEntity.ok(new TokenResponse(tokenData.getToken(), tokenData.getRefreshToken()));
    }

    @PostMapping(path = "/refresh")
    public ResponseEntity<TokenResponse> refresh(@RequestBody RefreshTokenRequest refreshTokenRequest) throws RefreshTokenException {
        TokenData tokenData = securityService.processRefreshToken(refreshTokenRequest.getRefreshToken());
        return ResponseEntity.ok(new TokenResponse(tokenData.getToken(), tokenData.getRefreshToken()));
    }

}
