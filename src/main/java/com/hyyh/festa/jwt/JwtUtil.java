package com.hyyh.festa.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hyyh.festa.domain.KakaoPublicKey;
import com.hyyh.festa.repository.KakaoPublicKeyRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.crypto.SecretKey;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Service
public class JwtUtil {
    private final SecretKey secretKey;
    private static final String AUTHORITIES_KEY = "authority";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000L * 60 * 60 * 24 * 30;

    public JwtUtil(KakaoPublicKeyRepository kakaoPublicKeyRepository) {
        // todo: 배포 시에는 32비트 비밀키 값 base64로 인코딩 => path var로 분리
        // 현재는 테스트용 비밀키
        secretKey = Keys.hmacShaKeyFor(
                Base64.getDecoder().decode("fgdfghdfhdfgdgdsgfdsgfdgdfsgdchgfbnfdntdrfgdfghdfhdfgdgdsgfdsgfdgdfsgdchgfbnfdntdrfgdfghdfhdfgdgdsgfdsgfdgdfsgdchgfbnfdntdrfgdfghdfhdfgdgdsgfdsgfdgdfsgdchgfbnfdntdr"));
    }

    public String generateToken(UserDetails userDetails) {
        String authorities = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        Date tokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        System.out.println("tokenExpiresIn = " + tokenExpiresIn);

        String accessToken = Jwts.builder()
                .subject(userDetails.getUsername())
                .issuer("HIFesta")
                .claim(AUTHORITIES_KEY, authorities)
                .expiration(tokenExpiresIn)
                .signWith(secretKey)
                .compact();

        validate(accessToken);

        return accessToken;
    }

    public void validate(String token) throws JwtException {
        try {
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secretKey).build().parseClaimsJws(token);
            System.out.println("claimsJws.getBody().get(\"sub\") = " + claimsJws.getBody().get("sub"));
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
    }

    public String extractUsertype(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
            return claims.get(AUTHORITIES_KEY, String.class);
        } catch (JwtException e) {
            log.error("토큰에서 사용자 타입을 추출할 수 없습니다: {}", e.getMessage());
            return null;
        }
    }

}
