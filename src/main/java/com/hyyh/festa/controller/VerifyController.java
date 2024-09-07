package com.hyyh.festa.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VerifyController {
    public ResponseEntity<?> verifyUser() {
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> verifyAdmin() {
        return ResponseEntity.ok().build();
    }
}
