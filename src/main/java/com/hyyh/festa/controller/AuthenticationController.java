package com.hyyh.festa.controller;

import com.hyyh.festa.dto.LoginRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {
    @PostMapping("/admin/login")
    public ResponseEntity<?> loginAdminUser(@RequestBody LoginRequest loginRequest) {
        return null;
    }
}
