package com.hyyh.festa.oidc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hyyh.festa.domain.KakaoPublicKey;
import com.hyyh.festa.repository.KakaoPublicKeyRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class OidcUtil {
    @Value("${KAKAO_REST_API_KEY}")
    private String REST_API_KEY;

    @Value("${KAKAO_REST_API_SECRET}")
    private String REST_API_SECRET;

    @Value("${KAKAO_REST_API_REDIRECT_URI}")
    private String REDIRECT_URI;

    private final KakaoPublicKeyRepository kakaoPublicKeyRepository;

    public String generateKakaoIdToken(String accessCode) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", REST_API_KEY);
        body.add("redirect_uri", REDIRECT_URI);
        body.add("code", accessCode);
        body.add("client_secret", REST_API_SECRET);

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();

        try {
            ResponseEntity<String> response = rt.exchange(
                    "https://kauth.kakao.com/oauth/token",
                    HttpMethod.POST,
                    kakaoTokenRequest,
                    String.class
            );

            String responseBody = response.getBody();
            if (responseBody == null) {
                throw new RuntimeException("카카오 API에서 응답 body를 얻는 중 문제가 발생했습니다.");
            }

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseBody);

            if (jsonNode.has("id_token")) {
                return jsonNode.get("id_token").asText();
            } else {
                throw new RuntimeException("카카오 API의 응답에 id_token이 없습니다.");
            }
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new RuntimeException("카카오 ID token을 얻는 중 HTTP 오류가 발생했습니다 : " + e.getMessage(), e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("카카오 API의 응답에서 JSON을 처리하는 과정에서 오류가 발생했습니다 : " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("예상치 못한 오류가 발생했습니다 : " + e.getMessage(), e);
        }
    }

    public String extractSubFromKakaoIdToken(String idToken) throws Exception {
        String jwksUrl = "https://kauth.kakao.com/.well-known/jwks.json";
        String kid = getKidFromToken(idToken);
        PublicKey publicKey = getPublicKey(kid, jwksUrl);

        Jws<Claims> claims = Jwts.parser()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(idToken);

        validateKakaoIdTokenClaims(claims.getBody());

        log.info("카카오 id 토큰 검증이 완료되었습니다.");
        return claims.getBody().getSubject();
    }

    private String getKidFromToken(String idToken) throws Exception {
        if (idToken == null || idToken.split("\\.").length != 3) {
            throw new IllegalArgumentException("유효하지 않은 토큰 형식입니다.");
        }

        try {
            String[] idTokenParts = idToken.split("\\.");
            String headerJson = new String(Base64.getUrlDecoder().decode(idTokenParts[0]));
            ObjectMapper mapper = new ObjectMapper();
            JsonNode header = mapper.readTree(headerJson);
            if (header.has("kid")) {
                return header.get("kid").asText();
            } else {
                throw new RuntimeException("헤더에 kid 필드가 없습니다.");
            }
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("JWT header를 디코딩하는 중 오류가 발생했습니다 : " + e.getMessage(), e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JWT header를 JSON으로 처리하는 중 오류가 발생했습니다 : " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("예상치 못한 오류가 발생했습니다 : " + e.getMessage(), e);
        }
    }

    private PublicKey getPublicKey(String kid, String jwksUrl) throws Exception {
        KakaoPublicKey kakaoPublicKey = kakaoPublicKeyRepository.findByKid(kid).orElse(null);

        if (kakaoPublicKey != null) {
            return createPublicKey(kakaoPublicKey.getN(), kakaoPublicKey.getE());
        }

        PublicKey publicKey = fetchAndStorePublicKey(kid, jwksUrl);
        if (publicKey == null) {
            throw new IllegalArgumentException("새로운 카카오 공개 키를 받는 중 오류가 발생했습니다.");
        }

        return publicKey;
    }

    private PublicKey createPublicKey(String n, String e) throws Exception {
        try {
            byte[] nBytes = Base64.getUrlDecoder().decode(n);
            byte[] eBytes = Base64.getUrlDecoder().decode(e);

            RSAPublicKeySpec spec = new RSAPublicKeySpec(
                    new BigInteger(1, nBytes),
                    new BigInteger(1, eBytes)
            );

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(spec);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Base64URL 문자열을 RSA 키 파라미터로 디코딩하는 중 오류가 발생했습니다 : " + ex.getMessage(), ex);
        } catch (Exception ex) {
            // Handle any other unexpected errors
            throw new RuntimeException("RSA 키를 만드는 중 예상치 못한 오류가 발생했습니다 : " + ex.getMessage(), ex);
        }

    }

    private PublicKey fetchAndStorePublicKey(String kid, String jwksUrl) throws Exception {
        URL url = new URL(jwksUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jwks = mapper.readTree(connection.getInputStream());

            for (JsonNode key : jwks.get("keys")) {
                if (key.get("kid").asText().equals(kid)) {
                    String n = key.get("n").asText();
                    String e = key.get("e").asText();
                    PublicKey publicKey = createPublicKey(n, e);

                    KakaoPublicKey kakaoPublicKey = new KakaoPublicKey();
                    kakaoPublicKey.setKid(kid);
                    kakaoPublicKey.setN(n);
                    kakaoPublicKey.setE(e);
                    kakaoPublicKey.setLastUpdated(LocalDateTime.now());
                    kakaoPublicKeyRepository.save(kakaoPublicKey);

                    return publicKey;
                }
            }
        } catch (IOException e) {
            throw new IOException("카카오에서 공개키를 가져오는 과정에서 오류가 발생했습니다 : " + e.getMessage(), e);
        }
        return null;
    }

    private void validateKakaoIdTokenClaims(Claims claims) {
        if (!claims.getIssuer().equals("https://kauth.kakao.com")) {
            throw new IllegalArgumentException("토큰 발행처가 다릅니다.");
        }
        if (claims.getExpiration().before(new Date())) {
            throw new IllegalArgumentException("토큰이 만료되었습니다.");
        }
        if (!claims.getAudience().contains(REST_API_KEY)) {
            throw new IllegalArgumentException("축제 사이트를 위한 토큰이 아닙니다.");
        }
    }

}
