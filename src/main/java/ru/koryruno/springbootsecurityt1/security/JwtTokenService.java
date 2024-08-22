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

@Service
@Slf4j
public class JwtTokenService {

//    private static final String ROLE_CLAIM = "role";
//    private static final String ID_CLAIM = "id";

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.token.expired}")
    private Duration tokenExpiration;

    @Value("${jwt.token.expired.refresh}")
    private Duration refreshTokenExpiration;

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

    public JwtAuthenticationDto refreshBaseToken(String username, String refreshToken) {
        JwtAuthenticationDto jwtDto = new JwtAuthenticationDto();
        jwtDto.setToken(generateJwtToken(username));
        jwtDto.setRefreshToken(refreshToken);
        return jwtDto;
    }

    public JwtAuthenticationDto generateAuthToken(String username) {
        JwtAuthenticationDto jwtDto = new JwtAuthenticationDto();
        jwtDto.setToken(generateJwtToken(username));
        jwtDto.setRefreshToken(generateRefreshToken(username));
        return jwtDto;
    }

    private String generateJwtToken(String username) {
        Date date = Date.from(LocalDateTime.now().plus(tokenExpiration).atZone(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                .setSubject(username)
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
}

//    @Value("${jwt.secret}")
//    private String secret;
//    @Value("${jwt.token.expired}")
//    private long tokenExpired;
//
//    public String generateToken(String username, List<String> roles) {
//        Map<String, Object> claims = new HashMap<>();
//        claims.put("roles", String.join(", ", roles));
//        return Jwts.builder()
//                .setClaims(claims)
//                .setSubject(username)
//                .setExpiration(new Date(System.currentTimeMillis() + tokenExpired))
//                .signWith(SignatureAlgorithm.HS512, secret)
//                .compact();
//    }
//
//    public String getUsernameFromToken(String token) {
//        Claims claims = getClaimsFromToken(token);
//        return claims.getSubject();
//    }
//
//    public List<String> getRoles(String token) {
//        Claims claims = getClaimsFromToken(token);
//        return Arrays.stream(claims.get("roles").toString().split(", "))
//                .toList();
//    }
//
//    public boolean validateToken(String token) {
//        try {
//            getClaimsFromToken(token);
//            return true;
//        } catch (JwtException | IllegalArgumentException e) {
//            throw new AuthenticationException("JWT token is expired or invalid");
//        }
//    }
//
//    private Claims getClaimsFromToken(String token) {
//        return Jwts.parser()
//                .setSigningKey(secret)
//                .parseClaimsJws(token)
//                .getBody();
//    }
//
//    public String generateRefreshToken() {
//        byte[] randomBytes = new byte[32];
//        new SecureRandom().nextBytes(randomBytes);
//        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
//    }