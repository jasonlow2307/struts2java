package com.deloitte.struts2java.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;

@Controller
@Tag(name = "Authentication", description = "API for user authentication")
public class LogoutController {

    @Operation(summary = "Log out user", description = "Logs out the current user and invalidates their session")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "Redirect to login page after logout")
    })
    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok().body("Logged out successfully");
    }
}