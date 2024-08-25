package com.github.ahmed_zein.ecommerce_backend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

@Configuration
public class WebSecurityConfig {
    private final JwtFilter jwtFilter;

    public WebSecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);

        http.addFilterBefore(jwtFilter, AuthorizationFilter.class);
        http.authorizeHttpRequests(
                authorize -> authorize
                        .requestMatchers(
                                "/error",
                                "/products",
                                "/auth/login",
                                "/auth/verify",
                                "/auth/register",
                                "/auth/account/reset-password",
                                "/auth/account/forgot-password",
                                "/swagger-resources/**",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/webjars/**")
                        .permitAll()
                        .anyRequest().authenticated());
        return http.build();
    }
}

/*
 * Key Points to Check:
 * 1. Swagger Endpoints:
 * - Ensure that all relevant Swagger paths are properly configured to be
 * permitted without authentication.
 * - Besides "/swagger-ui/**", you might also need to allow /v3/api-docs/** (for
 * OpenAPI 3.0), /swagger-resources/**, and /webjars/**.
 * 2. Swagger Paths:
 * - The path for Swagger UI might vary depending on your version of Springfox
 * or Springdoc (OpenAPI).
 * - Make sure the path you've used (/swagger-ui/**) matches the actual path in
 * your project.
 */