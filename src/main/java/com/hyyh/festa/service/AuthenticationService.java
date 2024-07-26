package com.hyyh.festa.service;

import com.hyyh.festa.domain.FestaUser;
import com.hyyh.festa.jwt.JwtUtil;
import com.hyyh.festa.oidc.OidcUtil;
import com.hyyh.festa.repository.FestaUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final FestaUserRepository festaUserRepository;
    private final JwtUtil jwtUtil;
    private final OidcUtil oidcUtil;

    public UserDetails authenticateAdminUser(String username, String password) {
        UsernamePasswordAuthenticationToken authenticateToken =
                UsernamePasswordAuthenticationToken.unauthenticated(username, password);

        Authentication authenticate = null;
        try {
            authenticate = authenticationManager.authenticate(authenticateToken);
        } catch (Exception e) {
            return null;
        }
        return (UserDetails) authenticate.getPrincipal();
    }

    public UserDetails authenticateFestaUser(String code) {
        // kakao oidc 인증
        String testKakaoSub = "1234567890";

        FestaUser festaUser = FestaUser.builder()
                .kakaoSub(testKakaoSub)
                .build();
        festaUserRepository.save(festaUser);

        return (UserDetails) festaUser;
    }
}
