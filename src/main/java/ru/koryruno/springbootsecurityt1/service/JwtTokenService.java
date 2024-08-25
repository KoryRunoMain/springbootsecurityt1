package ru.koryruno.springbootsecurityt1.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.koryruno.springbootsecurityt1.model.TokenData;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

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

    public TokenData generateAuthToken(String username, List<String> roles) {
        TokenData jwtDto = new TokenData();
        jwtDto.setToken(generateJwtToken(username, roles));
        jwtDto.setRefreshToken(generateRefreshToken(username));
        return jwtDto;
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public TokenData refreshBaseToken(String username, String refreshToken) {
        TokenData jwtDto = new TokenData();
        List<String> roles = getRolesFromToken(refreshToken);
        jwtDto.setToken(generateJwtToken(username, roles));
        jwtDto.setRefreshToken(refreshToken);
        return jwtDto;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(token)
                    .getBody();
            return true;
        } catch (Exception e) {
            log.error("invalid token", e);
        }
        return false;
    }

    private String generateJwtToken(String username, List<String> roles) {
        Date date = Date.from(LocalDateTime.now().plus(tokenExpiration).atZone(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                .setSubject(username)
                .claim(ROLE_CLAIM, roles)
                .setExpiration(date)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    private String generateRefreshToken(String username) {
        Date date = Date.from(LocalDateTime.now().plus(refreshTokenExpiration).atZone(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(date)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    private List<String> getRolesFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        return (List<String>) claims.get(ROLE_CLAIM, List.class);
    }

}