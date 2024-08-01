package com.hyyh.festa.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
public class TestController {
    @GetMapping("/test")
    public ResponseEntity<String> test(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity
                .status(200)
                .body("Hello " + userDetails.getUsername() + "! You are successfully authenticated.");
    }

    @GetMapping("/test/admin")
    public ResponseEntity<String> testAdmin(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity
                .status(200)
                .body("Hello '" + userDetails.getUsername() + "'. You are authenticated as " + getAuthority(userDetails) + ".");
    }

    @GetMapping("/test/user")
    public ResponseEntity<String> testUser(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity
                .status(200)
                .body("Hello '" + userDetails.getUsername() + "'. You are authenticated as " + getAuthority(userDetails) + ".");
    }

    private String getAuthority(UserDetails userDetails) {
        return userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining());
    }
}
