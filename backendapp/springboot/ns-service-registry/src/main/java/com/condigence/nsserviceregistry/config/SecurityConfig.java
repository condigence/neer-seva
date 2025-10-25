package com.condigence.nsserviceregistry.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // allow root and simple static assets
                .requestMatchers("/").permitAll()
                .requestMatchers("/index.html").permitAll()
                .requestMatchers("/static/**").permitAll()
                // allow all access to eureka endpoints for development (read + write)
                // IMPORTANT: remove or tighten this in production (use mTLS or client credentials)
                .requestMatchers("/eureka/**").permitAll()
                // actuator endpoints require authentication
                .requestMatchers("/actuator/**").authenticated()
                .anyRequest().permitAll()
            )
            .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
