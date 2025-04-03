package com.deloitte.controllers;

import com.deloitte.dto.ApiResponse;
import com.deloitte.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class MenuController {

    private final AuthenticationService authService;

    @GetMapping("/menu")
    public ResponseEntity<?> getMenu(HttpSession session) {
        if (!authService.isAuthenticated(session)) {
            return ResponseEntity.status(401)
                .body(ApiResponse.error("UNAUTHORIZED", "You must be logged in to access this page", null));
        }
        
        String username = (String) session.getAttribute(AuthenticationService.SESSION_USER_KEY);
        
        Map<String, Object> data = new HashMap<>();
        data.put("username", username);
        data.put("message", "Welcome to the system, " + username);
        
        // Add some menu options (this would be from a real service in a full app)
        Map<String, String> menuOptions = new HashMap<>();
        menuOptions.put("home", "/home");
        menuOptions.put("profile", "/profile");
        menuOptions.put("settings", "/settings");
        data.put("menuOptions", menuOptions);
        
        return ResponseEntity.ok(ApiResponse.success(data));
    }
}
