package com.hyyh.festa.controller;

import com.hyyh.festa.dto.KakaoLoginRequest;
import com.hyyh.festa.dto.LoginRequest;
import com.hyyh.festa.dto.TokenResponse;
import com.hyyh.festa.jwt.JwtUtil;
import com.hyyh.festa.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final JwtUtil jwtUtil;

    @PostMapping("/admin/token")
    public ResponseEntity<?> authenticateAdminUser(@RequestBody LoginRequest loginRequest) {
        UserDetails adminUser =
                authenticationService.authenticateAdminUser(loginRequest.getUsername(), loginRequest.getPassword());
        if (adminUser != null) {
            return ResponseEntity
                    .status(200)
                    .body(new TokenResponse(
                            "어드민 인가 토큰 발급",
                            jwtUtil.generateToken(adminUser)));
        } else {
            return ResponseEntity
                    .status(401)
                    .body("인증이 실패했다는 메시지");
        }
    }

    @PostMapping("/posts/token")
    public ResponseEntity<?> authenticatePostLost(@RequestBody KakaoLoginRequest kakaoLoginRequest) {
        UserDetails festaUser =
                authenticationService.authenticateFestaUser(kakaoLoginRequest.getCode());
        if (festaUser != null) {
            return ResponseEntity
                    .status(200)
                    .body(new TokenResponse(
                            "분실물 게시 토큰 발급",
                            jwtUtil.generateToken(festaUser)));
        } else {
            return ResponseEntity
                    .status(401)
                    .body("인증이 실패했다는 메시지");
        }
    }

    @PostMapping("/events/{eventId}/token")
    public ResponseEntity<?> authenticateEvent(@PathVariable Long eventId, @RequestBody KakaoLoginRequest kakaoLoginRequest) {
        UserDetails festaUser =
                authenticationService.authenticateFestaUser(kakaoLoginRequest.getCode());
        if (festaUser == null) {
            return ResponseEntity
                    .status(401)
                    .body("인증이 실패했다는 메시지");
        }
        if (!authenticationService.isEventApplicable(festaUser, eventId)) {
            return ResponseEntity
                    .status(400)
                    .body("응모가 불가능하다는 메시지");
        } else {
            return ResponseEntity
                    .status(200)
                    .body(jwtUtil.generateToken(festaUser));
        }
    }
}
