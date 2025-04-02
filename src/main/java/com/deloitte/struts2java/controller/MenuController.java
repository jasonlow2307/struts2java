package com.deloitte.struts2java.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@Tag(name = "Menu", description = "Menu management APIs")
public class MenuController {
    
    @Operation(
        summary = "Get Menu", 
        description = "Get menu items for the authenticated user",
        responses = {
            @ApiResponse(
                responseCode = "200", 
                description = "Successfully retrieved menu items",
                content = @Content(mediaType = "application/json")
            )
        }
    )
    @GetMapping("/menu")
    public ResponseEntity<Map<String, Object>> menu(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        String username = (String) request.getSession().getAttribute("loggedInUser");
        
        response.put("page", "menu");
        response.put("username", username);
        response.put("message", "Welcome to the menu, " + username);
        
        // You could add menu items or other data here
        Map<String, String> menuItems = new HashMap<>();
        menuItems.put("item1", "Dashboard");
        menuItems.put("item2", "Profile");
        menuItems.put("item3", "Settings");
        response.put("menuItems", menuItems);
        
        return ResponseEntity.ok(response);
    }
}