package com.nexus.catalog.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Typical for Stateless APIs
                .authorizeHttpRequests(auth -> auth
                        // 1. Permit OpenAPI / Swagger for documentation
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        // 2. Permit Actuator health checks (if needed for your Gateway)
                        .requestMatchers("/actuator/health", "/actuator/info").permitAll()
                        // 3. Authenticate everything else
                        .anyRequest().permitAll()
                )
                // 4. Tell Spring to act as a Resource Server (expecting Bearer Tokens)
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));

        return http.build();
    }

}
