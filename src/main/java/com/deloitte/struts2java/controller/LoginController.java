package com.deloitte.struts2java.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Controller
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

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(
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
            return "redirect:/menu";
            
        } else if (username != null && password != null && 
                  (!username.equals(storedUsername) || !password.equals(storedPassword))) {
            
            model.addAttribute("errorMessage", "Invalid username or password.");
            request.getSession().setAttribute("loggedInUser", null);
            return "login";
            
        } else {
            request.getSession().setAttribute("loggedInUser", null);
            return "login";
        }
    }
}