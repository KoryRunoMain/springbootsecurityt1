package ru.koryruno.springbootsecurityt1.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.koryruno.springbootsecurityt1.exception.NotFoundException;
import ru.koryruno.springbootsecurityt1.exception.RefreshTokenException;
import ru.koryruno.springbootsecurityt1.model.RefreshToken;
import ru.koryruno.springbootsecurityt1.repository.InMemoryRefreshTokenRepository;

import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final InMemoryRefreshTokenRepository refreshTokenRepository;

    @Value("${springbootsecurityt1.jwt.refreshTokenExpiration}")
    private Duration refreshTokenExpiration;

    public RefreshToken save(String userId) throws RefreshTokenException {
        String refreshTokenValue = UUID.randomUUID().toString();
        String id = UUID.randomUUID().toString();

        RefreshToken refreshToken = new RefreshToken(id, userId, refreshTokenValue);
        boolean isSuccess = refreshTokenRepository.save(refreshToken, refreshTokenExpiration);

        if (isSuccess) {
            return refreshToken;
        } else {
            throw new RefreshTokenException("Error on save refresh token for userId: " + userId);
        }
    }

    public RefreshToken getByValue(String refreshToken) {
        return refreshTokenRepository.findByValue(refreshToken)
                .orElseThrow(() -> new NotFoundException("RefreshToken not found"));
    }
}
