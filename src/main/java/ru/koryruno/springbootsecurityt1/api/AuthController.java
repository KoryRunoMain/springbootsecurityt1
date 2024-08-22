package ru.koryruno.springbootsecurityt1.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.koryruno.springbootsecurityt1.exception.AuthenticationException;
import ru.koryruno.springbootsecurityt1.model.dto.JwtAuthenticationDto;
import ru.koryruno.springbootsecurityt1.model.dto.RefreshTokenDto;
import ru.koryruno.springbootsecurityt1.model.dto.UserCredentialsDto;
import ru.koryruno.springbootsecurityt1.service.UserService;

@RestController
@RequestMapping(path = "/api/v1/public/token")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping("/password")
    public ResponseEntity<JwtAuthenticationDto> signIn(@RequestBody UserCredentialsDto userCredentialsDto) {
        try {
            JwtAuthenticationDto jwtAuthenticationDto = userService.signIn(userCredentialsDto);
            return ResponseEntity.ok(jwtAuthenticationDto);
        } catch (AuthenticationException e) {
            throw new AuthenticationException("Authentication failed" + e.getMessage());
        }
    }

    @PostMapping("/refresh")
    public JwtAuthenticationDto refresh(@RequestBody RefreshTokenDto refreshTokenDto) {
        return userService.refreshToken(refreshTokenDto);
    }

}
