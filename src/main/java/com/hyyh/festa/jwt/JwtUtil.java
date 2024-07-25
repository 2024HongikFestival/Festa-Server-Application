package com.hyyh.festa.jwt;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Base64;

@Service
public class JwtUtil {
    private final SecretKey secretKey;

    public JwtUtil() {
        // todo: 배포 시에는 32비트 비밀키 값 base64로 인코딩 => path var로 분리
        // 현재는 테스트용 비밀키
        secretKey = Keys.hmacShaKeyFor(
                Base64.getDecoder().decode("fgdfghdfhdfgdgdsgfdsgfdgdfsgdchgfbnfdntdrfgdfghdfhdfgdgdsgfdsgfdgdfsgdchgfbnfdntdr"));
    }

    public String generateToken(UserDetails userDetails) {
        return null;
    }

    public void validate(String token) throws JwtException {

    }

    public void validateKakaoOpenId(String idToken) throws Exception {

    }

    public String extractUsertype(String token) {
        return null;
    }
}
