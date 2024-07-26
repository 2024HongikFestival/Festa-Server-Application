package com.hyyh.festa.jwt;

import com.hyyh.festa.repository.AdminUserRepository;
import com.hyyh.festa.repository.FestaUserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final AdminUserRepository adminUserRepository;
    private final FestaUserRepository festaUserRepository;
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer")) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = header.substring(7);

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
            // todo: 인증 실패 응답
            filterChain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }
}
