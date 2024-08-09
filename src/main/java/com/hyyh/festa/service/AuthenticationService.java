package com.hyyh.festa.service;

import com.hyyh.festa.domain.FestaUser;
import com.hyyh.festa.domain.TokenType;
import com.hyyh.festa.oidc.KakaoErrorException;
import com.hyyh.festa.oidc.OidcUtil;
import com.hyyh.festa.repository.FestaUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final FestaUserRepository festaUserRepository;
    private final OidcUtil oidcUtil;

    public UserDetails authenticateAdminUser(String username, String password) {
        UsernamePasswordAuthenticationToken authenticateToken =
                UsernamePasswordAuthenticationToken.unauthenticated(username, password);

        Authentication authentication = null;
        try {
            authentication = authenticationManager.authenticate(authenticateToken);
        } catch (AuthenticationException e) {
            return null;
        }
        return (UserDetails) authentication.getPrincipal();
    }

    public UserDetails authenticateFestaUser(String code, TokenType tokenType) {
        String kakaoSub = null;
        try {
            String idToken = oidcUtil.generateKakaoIdToken(code, tokenType);
            kakaoSub = oidcUtil.extractSubFromKakaoIdToken(idToken);
        } catch (Exception e) {
            String eMessage = e.getMessage();
            String regex = "\"error_code\":\"(.*?)\"";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(eMessage);

            String matchString = "UNKNOWN";
            if (matcher.find()) {
                matchString = matcher.group(1);
            }
            throw new KakaoErrorException("인가 코드 처리 중 오류가 발생했습니다.", matchString);
        }

        FestaUser festaUser = festaUserRepository
                .findByKakaoSub(kakaoSub)
                .orElse(null);
        if (festaUser == null) {
            festaUser = FestaUser.builder()
                    .kakaoSub(kakaoSub)
                    .build();
            festaUserRepository.save(festaUser);
        }
        return festaUser;
    }
}
