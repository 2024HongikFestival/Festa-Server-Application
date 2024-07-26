package com.hyyh.festa.jwt;

import com.hyyh.festa.domain.AdminUser;
import com.hyyh.festa.repository.AdminUserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final AdminUserRepository adminUserRepository;
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // ‼️ for test
        // ‼️ for test
        // ‼️ for test
        final String header = request.getHeader("Authorization");
        if (header == null) {
            filterChain.doFilter(request, response);
            return;
        }
        String username = header.split(" ")[1].trim();
        AdminUser adminUser = adminUserRepository.findByUsername(username).orElse(null);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(adminUser, null, adminUser.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
        // ‼️ for test
        // ‼️ for test
        // ‼️ for test

//        final String header = request.getHeader("Authorization");
//        if (header == null || !header.startsWith("Bearer")) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//        String token = header.substring(7);
//
//        UserDetails userDetails = null;
//        if (jwtUtil.extractAuthority(token) == "ADMIN") {
//            userDetails = adminUserRepository
//                    .findByUsername(jwtUtil.extractSubject(token))
//                    .orElse(null);
//        } else {
//            userDetails = temporaryUserRepository
//                    .findBySubject(jwtUtil.extractSubject(token))
//                    .orElse(null);
//        }
//        if (userDetails == null) {
//            // todo: 인증 실패 응답
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        UsernamePasswordAuthenticationToken authenticationToken =
//                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//        authenticationToken.setDetails(new WebAuthenticationDetailsSource());
//        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//        filterChain.doFilter(request, response);
    }
}
