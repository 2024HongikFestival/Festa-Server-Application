package com.hyyh.festa.controller;

import com.hyyh.festa.domain.TokenType;
import com.hyyh.festa.dto.*;
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
        if (adminUser == null) {
            return ResponseEntity
                    .status(401)
                    .body(
                            ResponseDTO.unauthorized("어드민 인증 실패")
                    );
        }
        return ResponseEntity
                .status(200)
                .body(
                        ResponseDTO.ok(
                                "어드민 인가 토큰 발급",
                                new TokenResponse(jwtUtil.generateToken(adminUser))
                        )
                );
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
            String errorMessage = e.getMessage() + " - " + errorDesc;
            return ResponseEntity
                    .status(401)
                    .body(
                            ResponseDTO.unauthorized(errorMessage)
                    );
        }
        if (festaUser == null) {
            return ResponseEntity
                    .status(401)
                    .body(
                            ResponseDTO.unauthorized("일반 사용자 인증 실패")
                    );
        }
        if (validationService.isUserBlacklist(festaUser.getUsername())) {
            return ResponseEntity
                    .status(400)
                    .body(
                            ResponseDTO.unauthorized("차단된 사용자")
                    );
        }
        return ResponseEntity
                .status(200)
                .body(
                        ResponseDTO.ok(
                                "분실물 게시 인가 토큰 발급",
                                new TokenResponse(jwtUtil.generateToken(festaUser))
                        )
                );
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
                    .body(
                            ResponseDTO.unauthorized(e.getMessage() + " - " + errorDesc)
                    );
        }
        if (festaUser == null) {
            return ResponseEntity
                    .status(401)
                    .body(
                            ResponseDTO.unauthorized("일반 사용자 인증 실패")
                    );
        }
        if (validationService.isEventApplicable(eventId, festaUser) == 'n') {
            return ResponseEntity
                    .status(404)
                    .body(
                            ResponseDTO.notFound("존재하지 않는 이벤트입니다.")
                    );
        }
        else if (validationService.isEventApplicable(eventId, festaUser) == 'd') {
            return ResponseEntity
                    .status(409)
                    .body(
                            ResponseDTO.forbidden("한 이벤트에 중복 응모할 수 없습니다.")
                    );
        }
        else if (!validationService.isWithinArea(eventKakaoRequest.getLatitude(),eventKakaoRequest.getLongtitude(), 1)) {
            return ResponseEntity
                    .status(403)
                    .body(
                            ResponseDTO.forbidden("학교 바깥에서는 응모할 수 없습니다.")
                    );
        }
        else {
            return ResponseEntity
                    .status(200)
                    .body(
                            ResponseDTO.ok(
                                    "이벤트 응모 인가 토큰 발급",
                                    new TokenResponse(jwtUtil.generateToken(festaUser))
                            )
                    );
        }
    }
}
