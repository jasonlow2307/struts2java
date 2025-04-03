package com.deloitte.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Properties;

@Service
@Slf4j
public class AuthenticationService {

    private final Properties loginProperties = new Properties();
    
    // Session attribute names
    public static final String SESSION_USER_KEY = "username";
    public static final String SESSION_AUTH_KEY = "authenticated";

    public AuthenticationService() {
        try {
            // Load the login.properties file
            loginProperties.load(new ClassPathResource("login.properties").getInputStream());
            log.info("Loaded login properties successfully");
        } catch (IOException e) {
            log.error("Error loading login.properties", e);
            throw new RuntimeException("Error loading login.properties", e);
        }
    }

    public boolean authenticate(String username, String password, HttpSession session) {
        // Get the password for the username
        String storedPassword = loginProperties.getProperty(username);
        
        // Check if credentials are valid
        if (storedPassword != null && storedPassword.equals(password)) {
            // Store user information in session
            session.setAttribute(SESSION_USER_KEY, username);
            session.setAttribute(SESSION_AUTH_KEY, true);
            log.info("User {} successfully authenticated", username);
            return true;
        }
        
        log.warn("Failed login attempt for user: {}", username);
        return false;
    }
    
    public void logout(HttpSession session) {
        String username = (String) session.getAttribute(SESSION_USER_KEY);
        // Invalidate the session
        session.invalidate();
        log.info("User {} logged out", username);
    }
    
    public boolean isAuthenticated(HttpSession session) {
        return session.getAttribute(SESSION_AUTH_KEY) != null && 
               (boolean) session.getAttribute(SESSION_AUTH_KEY);
    }
}
