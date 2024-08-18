package ru.koryruno.springbootsecurityt1.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import ru.koryruno.springbootsecurityt1.model.AppUserPrincipal;

import java.security.Principal;
import java.time.Duration;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor

public class TokenService {
    private static final String ROLE_CLAIM = "role";
    private static final String ID_CLAIM = "id";

    @Value("${springbootsecurityt1.jwt.secret}")
    private String jwtSecret;

    @Value("${springbootsecurityt1.jwt.refreshTokenExpiration}")
    private Duration tokenExpiration;

    public String generateToken(String username, String id, List<String> roles) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + tokenExpiration.toMillis()))
                .claim(ROLE_CLAIM, roles)
                .claim(ID_CLAIM, id)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public Authentication toAuthentication(String token) {
        Claims tokenBody = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        String subject = tokenBody.getSubject();
        String id = tokenBody.get(ID_CLAIM, String.class);
        List<String> roles = (List<String>) tokenBody.get(ROLE_CLAIM);

        Principal principal = new AppUserPrincipal(subject, id, roles);

        return new UsernamePasswordAuthenticationToken(
                principal,
                null,
                roles.stream()
                        .map(SimpleGrantedAuthority::new)
                        .toList()
        );
    }
}
