package com.ysm.rprtrades.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())

            .sessionManagement(session ->
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            .authorizeHttpRequests(auth -> auth

                // -----------------------------------------------
                // PUBLIC PAGES  (no token needed)
                // -----------------------------------------------
                .requestMatchers(
                        "/",
                        "/home",
                        "/login",
                        "/register"
                ).permitAll()

                // -----------------------------------------------
                // STATIC RESOURCES
                // -----------------------------------------------
                .requestMatchers(
                        "/css/**",
                        "/js/**",
                        "/images/**",
                        "/fonts/**",
                        "/favicon.ico",
                        "/webjars/**"
                ).permitAll()

                // -----------------------------------------------
                // SWAGGER
                // -----------------------------------------------
                .requestMatchers(
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/v3/api-docs/**"
                ).permitAll()

                // -----------------------------------------------
                // PUBLIC APIS  (login + register)
                // -----------------------------------------------
                .requestMatchers(
                        "/api/users/login",
                        "/api/users/register"
                ).permitAll()

                // -----------------------------------------------
                // ALL UI PAGES — permitAll here.
                //
                // WHY: The browser cannot attach an Authorization
                // header on normal page navigation.  The JWT lives
                // in localStorage and is only accessible to JS.
                //
                // SECURITY MODEL:
                //   - UI pages are open at the HTTP level.
                //   - auth-guard.js redirects to /login if the
                //     token is absent or expired (client-side guard).
                //   - Every REST /api/** call carries the Bearer
                //     token and is protected by @PreAuthorize with
                //     role checks (server-side guard).
                //
                // This is the standard pattern for a stateless
                // JWT + Thymeleaf application.
                // -----------------------------------------------
                .requestMatchers(
                        "/dashboard/**",
                        "/products/**",
                        "/orders/**",
                        "/payments/**",
                        "/shipments/**",
                        "/documents/**",
                        "/invoices/**",
                        "/admin/**"
                ).permitAll()

                // -----------------------------------------------
                // ALL REST APIS — must carry a valid JWT
                // Role-level access is enforced via @PreAuthorize
                // on every controller method.
                // -----------------------------------------------
                .requestMatchers("/api/**").authenticated()

                // Anything else also requires auth
                .anyRequest().authenticated()
            )

            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
