package com.deloitte.struts2java.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@RestController
public class LoginController {
    
    private static final String PROPERTIES_FILE = "/login.properties";
    
    private String getProperty(String key) {
        Properties properties = new Properties();
        try (InputStream input = getClass().getResourceAsStream(PROPERTIES_FILE)) {
            if (input == null) {
                System.out.println("Unable to find " + PROPERTIES_FILE);
                return null;
            }
            properties.load(input);
            return properties.getProperty(key);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    

    @Operation(
        summary = "Login Page", 
        description = "Get the login page with a welcome message",
        responses = {
            @ApiResponse(
                responseCode = "200", 
                description = "Successfully retrieved login page",
                content = @Content(mediaType = "application/json")
            )
        }
    )
    @GetMapping("/login")
    public ResponseEntity<Map<String, String>> loginPage() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Please provide username and password");
        return ResponseEntity.ok(response);
    }
    
    @Operation(
        summary = "Authenticate User", 
        description = "Authenticate a user with username and password",
        responses = {
            @ApiResponse(
                responseCode = "200", 
                description = "Authentication successful",
                content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                responseCode = "401", 
                description = "Authentication failed",
                content = @Content(mediaType = "application/json")
            )
        }
    )
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(
            @RequestParam String username, 
            @RequestParam String password, 
            HttpServletRequest request) {
        
        Map<String, Object> response = new HashMap<>();
        
        // Fetch credentials from properties file
        String storedUsername = getProperty("username");
        String storedPassword = getProperty("password");
        
        // Validate login
        if (username != null && password != null && 
                username.equals(storedUsername) && password.equals(storedPassword)) {
            System.out.println("Login with " + username);
            request.getSession().setAttribute("loggedInUser", username);
            
            response.put("success", true);
            response.put("message", "Login successful");
            response.put("username", username);
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "Invalid username or password");
            request.getSession().setAttribute("loggedInUser", null);
            return ResponseEntity.status(401).body(response);
        }
    }

    @Operation(
        summary = "Logout User", 
        description = "Logout the current user and invalidate the session",
        responses = {
            @ApiResponse(
                responseCode = "200", 
                description = "Successfully logged out",
                content = @Content(mediaType = "application/json")
            )
        }
    )
    @GetMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        request.getSession().setAttribute("loggedInUser", null);
        request.getSession().invalidate();
        
        response.put("success", true);
        response.put("message", "Logged out successfully");
        return ResponseEntity.ok(response);
    }
}