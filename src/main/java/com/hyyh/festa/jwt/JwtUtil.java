package com.hyyh.festa.jwt;

import io.jsonwebtoken.JwtException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtUtil {
    public String generateToken(UserDetails userDetails) {

    }

    public void validate(String token) throws JwtException {

    }

    public void validateKakaoOpenId(String idToken) throws Exception {

    }

    public String extractUsertype(String token) {

    }
}
