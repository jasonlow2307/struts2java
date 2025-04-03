package com.deloitte.controllers;

import com.deloitte.dto.ApiResponse;
import com.deloitte.dto.LoginRequest;
import com.deloitte.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthenticationService authService;
    
    @GetMapping("/login")
    public ResponseEntity<ApiResponse<Map<String, String>>> getLoginPage() {
        Map<String, String> data = new HashMap<>();
        data.put("message", "Please log in with your username and password");
        return ResponseEntity.ok(ApiResponse.success(data));
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpSession session) {
        boolean authenticated = authService.authenticate(
            loginRequest.getUsername(), 
            loginRequest.getPassword(), 
            session
        );
        
        if (authenticated) {
            Map<String, Object> data = new HashMap<>();
            data.put("message", "Login successful");
            data.put("username", loginRequest.getUsername());
            data.put("redirectTo", "/menu");
            return ResponseEntity.ok(ApiResponse.success(data));
        } else {
            return ResponseEntity.status(401)
                .body(ApiResponse.error("INVALID_CREDENTIALS", "Invalid username or password", null));
        }
    }
    
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Map<String, String>>> logout(HttpSession session) {
        authService.logout(session);
        
        Map<String, String> data = new HashMap<>();
        data.put("message", "You have been successfully logged out");
        data.put("redirectTo", "/login");
        
        return ResponseEntity.ok(ApiResponse.success(data));
    }
}
