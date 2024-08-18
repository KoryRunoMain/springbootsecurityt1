package ru.koryruno.springbootsecurityt1.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.koryruno.springbootsecurityt1.exception.AuthException;
import ru.koryruno.springbootsecurityt1.exception.RefreshTokenException;
import ru.koryruno.springbootsecurityt1.model.RefreshToken;
import ru.koryruno.springbootsecurityt1.model.User;
import ru.koryruno.springbootsecurityt1.model.dto.TokenData;

@Service
@RequiredArgsConstructor
public class SecurityService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final RefreshTokenService refreshTokenService;

    public TokenData processPasswordToken(String username, String password) throws AuthException, RefreshTokenException {
        User user = userService.getUserByUsername(username);
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new AuthException("Invalid password for user: " + username);
        }
        return createTokenData(user);
    }

    public TokenData processRefreshToken(String refreshTokenValue) throws RefreshTokenException {
        RefreshToken refreshToken = refreshTokenService.getByValue(refreshTokenValue);
        User user = userService.getUserById(Long.valueOf(refreshToken.getUserId()));
        return createTokenData(user);
    }

    private TokenData createTokenData(User user) throws RefreshTokenException {
        String token = tokenService.generateToken(
                user.getUsername(),
                user.getId().toString(),
                user.getRoles().stream().map(Enum::name).toList()
        );
        RefreshToken refreshToken = refreshTokenService.save(user.getId().toString());
        return new TokenData(token, refreshToken.getValue());
    }

}
