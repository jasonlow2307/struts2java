package com.deloitte.struts2java.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("Pre-processing request: " + request.getRequestURI());

        String uri = request.getRequestURI();

        // Allow the login, logout, and welcome pages without checking for session
        if (uri.contains("/login") || uri.contains("/logout") || uri.endsWith("/welcome")) {
            return true;
        }

        // Check if user is logged in
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