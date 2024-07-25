package com.hyyh.festa.service;

import com.hyyh.festa.domain.AdminUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    public AdminUser loginAdminUser(String username, String password) {
        return null;
    }

    public UserDetails authenticateAdminUser(String username, String password) {
        UsernamePasswordAuthenticationToken authenticateToken =
                UsernamePasswordAuthenticationToken.unauthenticated(username, password);
        Authentication authenticate = authenticationManager.authenticate(authenticateToken);
        return (UserDetails) authenticate.getPrincipal();
    }
}
