package com.example.demo;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

@Component
@Order(1)
public class JwtRequestFilter extends OncePerRequestFilter {
    @Autowired
    private CustomUserDetailsService userDetailsService;
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest httpRequest, HttpServletResponse httpResponse, FilterChain chain)
            throws IOException, ServletException {
        String path = httpRequest.getServletPath();

        System.out.println("JWT Filter - Processing path: " + path);
        
        // Skip JWT check for auth endpoints only
        if (path.startsWith("/auth/")) {
            System.out.println("JWT Filter - Skipping authentication for auth endpoint: " + path);
            chain.doFilter(httpRequest, httpResponse);
            return;
        }
        
        System.out.println("JWT Filter - Proceeding with JWT authentication for: " + path);

        final String authHeader = httpRequest.getHeader("Authorization");
        String username = null;
        String jwt = null;

        System.out.println("JWT Filter - Request URI: " + httpRequest.getRequestURI());
        System.out.println("JWT Filter - Authorization header: " + (authHeader != null ? "Present" : "Missing"));

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            username = jwtUtil.extractUsername(jwt);
            System.out.println("JWT Filter - Extracted username: " + username);
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                System.out.println("JWT Filter - User details loaded: " + userDetails.getUsername());
                System.out.println("JWT Filter - User authorities: " + userDetails.getAuthorities());
                
                if (jwtUtil.validateToken(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    System.out.println("JWT Filter - Authentication set successfully");
                } else {
                    System.out.println("JWT Filter - Token validation failed");
                }
            } catch (Exception e) {
                System.err.println("JWT Filter - Error processing authentication: " + e.getMessage());
                e.printStackTrace();
            }
        } else if (username == null) {
            System.out.println("JWT Filter - No username extracted from token");
        } else {
            System.out.println("JWT Filter - Authentication already exists");
        }
        
        chain.doFilter(httpRequest, httpResponse);
    }
} 