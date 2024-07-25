package com.hyyh.festa.controller;

import com.hyyh.festa.dto.LoginRequest;
import com.hyyh.festa.dto.TokenResponse;
import com.hyyh.festa.jwt.JwtUtil;
import com.hyyh.festa.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final JwtUtil jwtUtil;

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
}
