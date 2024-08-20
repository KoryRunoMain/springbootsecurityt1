package ru.koryruno.springbootsecurityt1.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.koryruno.springbootsecurityt1.exception.UnauthorizedException;
import ru.koryruno.springbootsecurityt1.model.User;
import ru.koryruno.springbootsecurityt1.model.dto.PasswordTokenRequest;
import ru.koryruno.springbootsecurityt1.model.dto.RefreshTokenRequest;
import ru.koryruno.springbootsecurityt1.model.dto.TokenResponse;
import ru.koryruno.springbootsecurityt1.service.TokenService;
import ru.koryruno.springbootsecurityt1.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/public/token")
@RequiredArgsConstructor
public class TokenController {

    private final TokenService tokenService;
    private final UserService userService;

    @PostMapping("/password")
    public TokenResponse password(@RequestBody PasswordTokenRequest passwordTokenRequest) throws UnauthorizedException {
        User user = userService.getUserByUsername(passwordTokenRequest.getUsername());
        if (user != null && tokenService.validateToken(passwordTokenRequest.getPassword())) {
            String token = tokenService.generateToken(user.getUsername(), user.getId().toString(), List.of(user.getRoles()));
            return new TokenResponse(token, generateRefreshToken(user.getId().toString()));
        } else {
            throw new UnauthorizedException("Invalid username or password");
        }
    }

    @PostMapping("/refresh")
    public TokenResponse refresh(@RequestBody RefreshTokenRequest request) {
        // Implement refresh token logic here
        return new TokenResponse();
    }

    private String generateRefreshToken(String userId) {
        // Implement logic to generate a refresh token
        return "refreshToken";
    }

}
