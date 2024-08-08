package com.hyyh.festa.config;

import com.hyyh.festa.domain.AdminUser;
import com.hyyh.festa.jwt.JwtAuthenticationFilter;
import com.hyyh.festa.repository.AdminUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final AdminUserRepository adminUserRepository;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .csrf(CsrfConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // 인증 인가 테스트
                        .requestMatchers("/test").authenticated()
                        .requestMatchers("/test/admin").hasAuthority("ADMIN")
                        .requestMatchers("/test/user").hasAuthority("USER")

                        // 인가 토큰 발급
                        .requestMatchers("/admin/token").permitAll()
                        .requestMatchers("/losts/token").permitAll()
                        .requestMatchers("/events/*/token").permitAll()

                        // 어드민 권한
                        .requestMatchers("/admin/**").hasAuthority("ADMIN")

                        // 일반 사용자 권한
                        .requestMatchers("/events/*/entries").hasAuthority("USER")
                        .requestMatchers("/booth/*/like").hasAuthority("USER")
                        // todo: POST /losts 설정 (구현 이후 적용 가능)

                        // 부스
                        .requestMatchers("/booths").permitAll()
                        .requestMatchers("/booths/**").permitAll()

                        // 그 외
                        .anyRequest().permitAll()
                )

                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(authenticationProvider);
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        // ‼️ for test
        // ‼️ for test
        AdminUser adminUser = AdminUser.builder()
                .username("admin")
                .password(passwordEncoder.encode("0000"))
                .build();
        adminUserRepository.save(adminUser);

        return username -> adminUserRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("유저가 없다는 메시지"));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
