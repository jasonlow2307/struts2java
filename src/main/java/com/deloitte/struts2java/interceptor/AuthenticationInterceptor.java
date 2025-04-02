package com.deloitte.struts2java.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthenticationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("Pre-processing request: " + request.getRequestURI());

        String uri = request.getRequestURI();
        if (uri.endsWith("/login") || uri.endsWith("/logout") || uri.contains("/css/") 
                || uri.contains("/js/") || uri.contains("/images/")) {
            // Allow login, logout, and static resources without checking for session
            return true;
        }

        // Check if a user is logged in
        String loggedInUser = (String) request.getSession().getAttribute("loggedInUser");
        System.out.println(":::::::::INTERCEPTOR::::::::::loggedInUser = " + loggedInUser);
        
        if (loggedInUser == null) {
            System.out.println("User not logged in. Redirecting to login page.");
            response.sendRedirect("/login");
            return false;
        }

        return true;
    }
}