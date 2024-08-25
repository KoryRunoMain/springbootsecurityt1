package ru.koryruno.springbootsecurityt1.service.impl;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.koryruno.springbootsecurityt1.exception.AuthException;
import ru.koryruno.springbootsecurityt1.exception.NotFoundException;
import ru.koryruno.springbootsecurityt1.model.User;
import ru.koryruno.springbootsecurityt1.model.UserRole;
import ru.koryruno.springbootsecurityt1.model.requestDto.RefreshTokenRequest;
import ru.koryruno.springbootsecurityt1.model.responseDto.TokenResponse;
import ru.koryruno.springbootsecurityt1.model.requestDto.UserCredentialsRequest;
import ru.koryruno.springbootsecurityt1.model.mapper.TokenMapper;
import ru.koryruno.springbootsecurityt1.repository.UserRepository;
import ru.koryruno.springbootsecurityt1.service.JwtTokenService;
import ru.koryruno.springbootsecurityt1.service.TokenService;

import java.util.List;
import java.util.Optional;

@Data
@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    private final UserRepository userRepository;
    private final JwtTokenService jwtTokenService;
    private final PasswordEncoder passwordEncoder;
    private final TokenMapper tokenMapper;

    @Override
    public TokenResponse signIn(UserCredentialsRequest userCredentialsDto) {
        User user = findByCredentials(userCredentialsDto);
        List<String> roles = user.getRoles().stream()
                .map(UserRole::getRoleName)
                .toList();
        return tokenMapper.toTokenResponse(jwtTokenService.generateAuthToken(user.getUsername(), roles));
    }

    @Override
    public TokenResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        String refreshToken = refreshTokenRequest.getRefreshToken();
        if (refreshToken != null && jwtTokenService.validateToken(refreshToken)) {
            User user = userRepository.findByUsername(jwtTokenService.getUsernameFromToken(refreshToken))
                    .orElseThrow(() -> new NotFoundException("User not found"));

            return tokenMapper.toTokenResponse(jwtTokenService.refreshBaseToken(user.getUsername(), refreshToken));
        }
        throw new AuthException("Invalid refresh token");
    }

    private User findByCredentials(UserCredentialsRequest userCredentialsDto) throws AuthException {
        Optional<User> optionalUser = userRepository.findByUsername(userCredentialsDto.getUsername());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (passwordEncoder.matches(userCredentialsDto.getPassword(), user.getPassword())) {
                return user;
            }
        }
        throw new AuthException("Username or password not correct");
    }

}
