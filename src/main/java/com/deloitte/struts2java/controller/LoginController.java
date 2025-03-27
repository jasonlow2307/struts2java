package com.deloitte.struts2java.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

@Controller
@Tag(name = "Authentication", description = "API for user authentication")
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
//
//    @Operation(summary = "Show login page", description = "Displays the login page to the user")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Login page displayed successfully")
//    })
//    @GetMapping("/login")
//    public String showLoginPage() {
//        return "login";
//    }

    @Operation(summary = "Process login", description = "Authenticates the user with provided credentials")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful or unsuccessful"),
            @ApiResponse(responseCode = "302", description = "Redirect to menu page on successful login")
    })
    @PostMapping("/login")
    public ResponseEntity<?> processLogin(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String password,
            HttpServletRequest request,
            Model model) {

        // Fetch credentials from properties file
        String storedUsername = getProperty("username");
        String storedPassword = getProperty("password");

        // Validate login
        if (username != null && password != null &&
                username.equals(storedUsername) && password.equals(storedPassword)) {

            System.out.println("Login with " + username);
            request.getSession().setAttribute("loggedInUser", username);

            // Return JSON response instead of redirecting
            return ResponseEntity.ok().body(Map.of(
                    "username", username,
                    "password", password
            ));
        }  else if (username != null && password != null &&
                (!username.equals(storedUsername) || !password.equals(storedPassword))) {

            model.addAttribute("errorMessage", "Invalid username or password.");
            request.getSession().setAttribute("loggedInUser", null);
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Invalid username or password"
            ));

        } else {
            request.getSession().setAttribute("loggedInUser", null);
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Invalid input"
            ));
        }
    }
}