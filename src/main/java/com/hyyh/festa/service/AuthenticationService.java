package com.hyyh.festa.service;

import com.hyyh.festa.domain.FestaUser;
import com.hyyh.festa.oidc.OidcUtil;
import com.hyyh.festa.repository.FestaUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

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

    public UserDetails authenticateFestaUser(String code) {
        String kakaoSub = null;
        try {
            String idToken = oidcUtil.generateKakaoIdToken(code);
            kakaoSub = oidcUtil.extractSubFromKakaoIdToken(idToken);
        } catch (Exception e) {
            return null;
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

    public boolean isEventApplicable(UserDetails festaUser, Long eventId) {
        return true;
    }
}
