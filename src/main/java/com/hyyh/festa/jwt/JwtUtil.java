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

    public String extractUsertype(String token) {

    }
}
