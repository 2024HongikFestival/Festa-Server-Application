package com.hyyh.festa.config;

import com.hyyh.festa.domain.AdminUser;
import com.hyyh.festa.jwt.JwtAuthenticationFilter;
import com.hyyh.festa.repository.AdminUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
                        // 인증, 인가 테스트
                        .requestMatchers(HttpMethod.GET, "/test").authenticated()
                        .requestMatchers(HttpMethod.GET, "/test/admin").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/test/user").hasAuthority("USER")

                        // 어드민 - 인증
                        .requestMatchers(HttpMethod.POST, "/admin/token").permitAll()

                        // 어드민 - 이벤트
                        .requestMatchers(HttpMethod.POST, "/admin/events").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/admin/events/up").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/admin/events/*").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/admin/events/*").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/admin/events/*/entries").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/admin/events/*/entries/*").hasAuthority("ADMIN")

                        // 어드민 - 분실물
                        .requestMatchers(HttpMethod.DELETE, "/admin/losts/*").hasAuthority("ADMIN")

                        // 어드민 - 블랙리스트
                        .requestMatchers(HttpMethod.POST, "/admin/blacklist").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/admin/blacklist").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/admin/blacklist/*").hasAuthority("ADMIN")

                        // 일반 사용자 - 인증
                        .requestMatchers(HttpMethod.POST, "/losts/token").permitAll()
                        .requestMatchers(HttpMethod.POST, "/events/*/token").permitAll()

                        // 일반 사용자 - 이벤트
                        .requestMatchers(HttpMethod.GET, "/events").permitAll()
                        .requestMatchers(HttpMethod.GET, "/events/*").permitAll()
                        .requestMatchers(HttpMethod.POST, "/events/*/entries").hasAuthority("USER")

                        // 일반 사용자 - 분실물
                        .requestMatchers(HttpMethod.POST, "/losts").hasAuthority("USER")
                        .requestMatchers(HttpMethod.GET, "/losts").permitAll()
                        .requestMatchers(HttpMethod.GET, "/losts/up").hasAuthority("USER")
                        .requestMatchers(HttpMethod.GET, "/losts/*").permitAll()

                        // 일반 사용자 - 부스
                        .requestMatchers(HttpMethod.GET, "/booths").permitAll()
                        .requestMatchers(HttpMethod.GET, "/booths/*").permitAll()
                        .requestMatchers(HttpMethod.POST, "/booths/*/like").permitAll()
                        .requestMatchers("/subscribe").permitAll()

                        // 부스
                        .requestMatchers("/booths").permitAll()
                        .requestMatchers("/booths/**").permitAll()

                        // 그 외
                        .anyRequest().denyAll()
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
//        // ‼️ for test
//        // ‼️ for test
//        AdminUser adminUser = AdminUser.builder()
//                .username("admin")
//                .password(passwordEncoder.encode("0000"))
//                .build();
//        adminUserRepository.save(adminUser);

        return username -> adminUserRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자가 존재하지 않습니다."));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
