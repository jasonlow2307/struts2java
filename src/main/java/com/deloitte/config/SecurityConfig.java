package com.deloitte.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // For simplicity, we're disabling CSRF and allowing all requests
        // In a real application, proper authentication would be implemented
        http.csrf().disable()
            .authorizeRequests()
            .antMatchers("/api/**").permitAll()
            .antMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
            .antMatchers("/h2-console/**").permitAll()
            .anyRequest().authenticated();
        
        // Allow frames for H2 console
        http.headers().frameOptions().sameOrigin();
        
        return http.build();
    }
}
