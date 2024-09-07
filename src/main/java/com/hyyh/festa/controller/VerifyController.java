package com.hyyh.festa.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VerifyController {
    @GetMapping("/test/user")
    public ResponseEntity<?> verifyUser() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/test/admin")
    public ResponseEntity<?> verifyAdmin() {
        return ResponseEntity.ok().build();
    }
}
