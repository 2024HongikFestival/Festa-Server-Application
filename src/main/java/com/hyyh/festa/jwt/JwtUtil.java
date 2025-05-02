package com.hyyh.festa.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.stream.Collectors;

@Service
public class JwtUtil {
    private final SecretKey secretKey;
    private static final String AUTHORITIES_KEY = "authority";
    private static final long ADMIN_EXPIRE_TIME = 1000L * 60 * 60 * 24;
    private static final long USER_EXPIRE_TIME = 1000L * 60 * 5;

    public JwtUtil(@Value("${SECRET_KEY}") String encodedSecretKey) {
        secretKey = Keys.hmacShaKeyFor(
                Base64.getDecoder().decode(encodedSecretKey));
    }

    public String generateToken(UserDetails userDetails) {
        String authorities = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        Date tokenExpiresIn = null;
        if (authorities.equals("ADMIN")) {
            tokenExpiresIn = new Date(now + ADMIN_EXPIRE_TIME);
        } else {
            tokenExpiresIn = new Date(now + USER_EXPIRE_TIME);
        }

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuer("HIFesta")
                .claim(AUTHORITIES_KEY, authorities)
                .expiration(tokenExpiresIn)
                .signWith(secretKey)
                .compact();
    }

    public void validate(String token) throws JwtException, IllegalArgumentException {
        Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token);
    }

    public String extractUsertype(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .get(AUTHORITIES_KEY, String.class);
        } catch (Exception e) {
            return null;
        }
    }

    public String extractSubject(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
        } catch (JwtException | IllegalArgumentException e) {
            return null;
        }
    }
}
