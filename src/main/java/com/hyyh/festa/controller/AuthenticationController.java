package com.hyyh.festa.controller;

import com.hyyh.festa.domain.FestaUser;
import com.hyyh.festa.dto.KakaoLoginRequest;
import com.hyyh.festa.dto.LoginRequest;
import com.hyyh.festa.dto.TokenResponse;
import com.hyyh.festa.jwt.JwtUtil;
import com.hyyh.festa.oidc.OidcUtil;
import com.hyyh.festa.repository.FestaUserRepository;
import com.hyyh.festa.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final FestaUserRepository festaUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final OidcUtil oidcUtil;

    @PostMapping("/admin/login")
    public ResponseEntity<?> loginAdminUser(@RequestBody LoginRequest loginRequest) {
        UserDetails adminUser =
                authenticationService.authenticateAdminUser(loginRequest.getUsername(), loginRequest.getPassword());
        if (adminUser != null) {
            return ResponseEntity
                    .status(200)
                    .body(new TokenResponse(jwtUtil.generateToken(adminUser)));
        } else {
            return ResponseEntity
                    .status(401)
                    .body("인증이 실패했다는 메시지");
        }
    }

    @PostMapping("/kakao/login")
    public ResponseEntity<?> loginFestaUser(@RequestBody KakaoLoginRequest kakaoLoginRequest) throws Exception {
        // ‼️ for test
        // ‼️ for test
        String idToken = oidcUtil.generateKakaoIdToken(kakaoLoginRequest.getAccessCode());
        String kakaoSub = oidcUtil.extractSubFromKakaoIdToken(idToken);

        FestaUser findFestaUser = festaUserRepository.findByKakaoSub(kakaoSub).orElse(null);

        if (findFestaUser != null) {
            return ResponseEntity
                    .status(200)
                    .body(jwtUtil.generateToken(findFestaUser));
        } else {
            FestaUser newFestaUser = FestaUser.builder()
                    .kakaoSub(kakaoSub)
                    .password(passwordEncoder.encode("abc"))
                    .build();
            festaUserRepository.save(newFestaUser);
            return ResponseEntity
                    .status(200)
                    .body(jwtUtil.generateToken(newFestaUser));
        }
    }
}
