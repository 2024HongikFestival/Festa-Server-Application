package com.hyyh.festa.controller;

import com.hyyh.festa.domain.TokenType;
import com.hyyh.festa.dto.EventKakaoRequest;
import com.hyyh.festa.dto.KakaoLoginRequest;
import com.hyyh.festa.dto.LoginRequest;
import com.hyyh.festa.dto.TokenResponse;
import com.hyyh.festa.jwt.JwtUtil;
import com.hyyh.festa.oidc.KakaoErrorException;
import com.hyyh.festa.service.AuthenticationService;
import com.hyyh.festa.service.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final ValidationService validationService;
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

    @PostMapping("/losts/token")
    public ResponseEntity<?> authenticatePostLost(@RequestBody KakaoLoginRequest kakaoLoginRequest) {
        UserDetails festaUser;
        try {
            festaUser = authenticationService.authenticateFestaUser(kakaoLoginRequest.getCode(), TokenType.LOST);
        } catch (KakaoErrorException e) {
            String errorCode = e.getErrorCode();
            String errorDesc;
            if (Objects.equals(errorCode, "KOE320")) {
                errorDesc = "만료된 인가코드입니다. 인가 코드는 일회용입니다.";
            }
            else if (Objects.equals(errorCode, "KOE303")) {
                errorDesc = "잘못된 redirect URI입니다. 요청할 토큰 종류와 redirect URI가 잘 맞는지 확인하세요.";
            }
            else {
                errorDesc = "예상치 못한 오류입니다.";
            }
            return ResponseEntity
                    .status(401)
                    .body(e.getMessage() + "\n" + errorDesc);
        }
        if (festaUser == null) {
            return ResponseEntity
                    .status(401)
                    .body("인증이 실패했다는 메시지");
        }
        if (validationService.isUserBlacklist(festaUser.getUsername())) {
            return ResponseEntity
                    .status(400)
                    .body("해당 사용자가 블랙리스트에 있습니다.");
        }
        return ResponseEntity
                .status(200)
                .body(new TokenResponse(
                        "분실물 게시 토큰 발급",
                        jwtUtil.generateToken(festaUser)));
    }

    @PostMapping("/events/{eventId}/token")
    public ResponseEntity<?> authenticateEvent(@PathVariable Long eventId, @RequestBody EventKakaoRequest eventKakaoRequest) {
        UserDetails festaUser;
        try {
            festaUser = authenticationService.authenticateFestaUser(eventKakaoRequest.getCode(), TokenType.ENTRY);
        } catch (KakaoErrorException e) {
            String errorCode = e.getErrorCode();
            String errorDesc;
            if (Objects.equals(errorCode, "KOE320")) {
                errorDesc = "만료된 인가코드입니다. 인가 코드는 일회용입니다.";
            }
            else if (Objects.equals(errorCode, "KOE303")) {
                errorDesc = "잘못된 redirect URI입니다. 요청할 토큰 종류와 redirect URI가 잘 맞는지 확인하세요.";
            }
            else {
                errorDesc = "예상치 못한 오류입니다.";
            }
            return ResponseEntity
                    .status(401)
                    .body(e.getMessage() + "\n" + errorDesc);
        }
        if (festaUser == null) {
            return ResponseEntity
                    .status(401)
                    .body("인증이 실패했다는 메시지");
        }
        if (!validationService.isEventApplicable(eventId)) {
            return ResponseEntity
                    .status(400)
                    .body("응모가 불가능하다는 메시지");
        }
        else if (!validationService.isWithinArea(eventKakaoRequest.getLatitude(),eventKakaoRequest.getLongtitude())) {
            return ResponseEntity
                    .status(400)
                    .body("위치 검증에 실패했다는 메시지");
        }
        else {
            return ResponseEntity
                    .status(200)
                    .body(jwtUtil.generateToken(festaUser));
        }
    }
}
