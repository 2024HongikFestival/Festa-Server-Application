package com.hyyh.festa.jwt;

import com.hyyh.festa.repository.AdminUserRepository;
import com.hyyh.festa.repository.FestaUserRepository;
import com.hyyh.festa.repository.JwtBlacklistRepository;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtBlacklistRepository jwtBlacklistRepository;
    private final AdminUserRepository adminUserRepository;
    private final FestaUserRepository festaUserRepository;
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 인가 토큰 추출
        final String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer")) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = header.split(" ")[1].trim();

        // 블랙 리스트 검사
        if (jwtBlacklistRepository.findByToken(token).isPresent()) {
            filterChain.doFilter(request, response);
            return;
        }

        // 유효성 검사
        try {
            jwtUtil.validate(token);
        } catch (JwtException | IllegalArgumentException e) {
            filterChain.doFilter(request, response);
            return;
        }

        // 사용자, 권한 식별
        UserDetails userDetails = null;
        if (jwtUtil.extractUsertype(token).equals("ADMIN")) {
            userDetails = adminUserRepository
                    .findByUsername(jwtUtil.extractSubject(token))
                    .orElse(null);
        } else {
            userDetails = festaUserRepository
                    .findByKakaoSub(jwtUtil.extractSubject(token))
                    .orElse(null);
        }
        if (userDetails == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // Spring security context에 등록
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }
}
