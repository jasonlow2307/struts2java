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
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Authorize all requests
            .authorizeRequests()
                .antMatchers("/**").permitAll()
                .and()
            // Disable form login
            .formLogin().disable()
            // Disable HTTP Basic
            .httpBasic().disable()
            // Disable CSRF - this will fix the 403 error
            .csrf().disable();
        
        return http.build();
    }
}
