package com.example.userservice.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // CSRF 보호 비활성화 (Stateless API에서는 보통 비활성화)
            .csrf(csrf -> csrf.disable())
            // 세션을 사용하지 않도록 설정 (Stateless API)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // HTTP 요청에 대한 접근 권한 설정
            .authorizeHttpRequests(authz -> authz
            	    // "/users/signup"과 "/users/login" 경로 허용
            	    .requestMatchers("/users/signup", "/users/login").permitAll()
            	    .anyRequest().authenticated()
            );
        return http.build();
    }
}