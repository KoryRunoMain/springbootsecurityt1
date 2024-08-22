package ru.koryruno.springbootsecurityt1.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.koryruno.springbootsecurityt1.model.dto.JwtAuthenticationDto;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class JwtTokenService {

    private static final String ROLE_CLAIM = "roles";

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.token.expired}")
    private Duration tokenExpiration;

    @Value("${jwt.token.expired.refresh}")
    private Duration refreshTokenExpiration;

    public JwtAuthenticationDto generateAuthToken(String username, List<String> roles) {
        JwtAuthenticationDto jwtDto = new JwtAuthenticationDto();
        jwtDto.setToken(generateJwtToken(username, roles));
        jwtDto.setRefreshToken(generateRefreshToken(username));
        return jwtDto;
    }

    private String generateJwtToken(String username, List<String> roles) {
        Date date = Date.from(LocalDateTime.now().plus(tokenExpiration).atZone(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                .setSubject(username)
                .claim(ROLE_CLAIM, roles)
                .setExpiration(date)
                .signWith(SignatureAlgorithm.ES512, jwtSecret)
                .compact();
    }

    private String generateRefreshToken(String username) {
        Date date = Date.from(LocalDateTime.now().plus(refreshTokenExpiration).atZone(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(date)
                .signWith(SignatureAlgorithm.ES512, jwtSecret)
                .compact();
    }

    public JwtAuthenticationDto refreshBaseToken(String username, String refreshToken) {
        JwtAuthenticationDto jwtDto = new JwtAuthenticationDto();
        List<String> roles = getRolesFromToken(refreshToken); // TODO Добавлена строка
        jwtDto.setToken(generateJwtToken(username, roles));
        jwtDto.setRefreshToken(refreshToken);
        return jwtDto;
    }

    // TODO Добавлен метод
    private List<String> getRolesFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        return claims.get(ROLE_CLAIM, List.class);
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(token)
                    .getBody();
            return true;
        } catch (Exception exception) {
            log.error("invalid token", exception);
        }
        return false;
    }
}