package com.hyyh.festa.controller;

import com.hyyh.festa.domain.AdminUser;
import com.hyyh.festa.dto.LoginRequest;
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
    @PostMapping("/admin/login")
    public ResponseEntity<String> loginAdminUser(@RequestBody LoginRequest loginRequest) {
        System.out.println(loginRequest);
        UserDetails adminUser =
                authenticationService.authenticateAdminUser(loginRequest.getUsername(), loginRequest.getPassword());
        System.out.println(adminUser.getUsername());
        return ResponseEntity.status(200).body(adminUser.getUsername());
    }
}
