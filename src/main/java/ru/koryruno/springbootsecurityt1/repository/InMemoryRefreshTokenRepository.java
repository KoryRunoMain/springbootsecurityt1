package ru.koryruno.springbootsecurityt1.repository;

import org.springframework.stereotype.Repository;
import ru.koryruno.springbootsecurityt1.model.RefreshToken;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryRefreshTokenRepository {

    private final Map<String, RefreshToken> tokens = new ConcurrentHashMap<>();

    public Optional<RefreshToken> findByValue(String tokenValue) {
        return Optional.ofNullable(tokens.get(tokenValue));
    }

    public boolean save(RefreshToken refreshToken, Duration expiration) {
        tokens.put(refreshToken.getValue(), refreshToken);
        return true;
    }

    public void deleteByUserId(String userId) {
        tokens.values().removeIf(token -> token.getUserId().equals(userId));
    }

}
