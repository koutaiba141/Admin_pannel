package com.example.demo;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.core.annotation.Order;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Order(0)
public class IpConnectionRateLimitFilter extends OncePerRequestFilter {
    @Autowired
    private IpConfigService ipConfigService;
    private static final int MAX_CONNECTIONS_PER_IP = 5;
    private static final int MAX_SESSIONS_PER_USER = 3;
    private static final int MAX_REQUESTS_PER_USER_PER_MIN = 30;
    private static final int MAX_LOGIN_ATTEMPTS_PER_5MIN = 5;
    private static final int MAX_WRITE_ACTIONS_PER_MIN = 10;

    private final Map<String, AtomicInteger> ipConnectionCount = new ConcurrentHashMap<>();
    private final Map<String, AtomicInteger> userSessionCount = new ConcurrentHashMap<>();
    private final Map<String, UserRateLimit> userRateLimits = new ConcurrentHashMap<>();
    private final Map<String, UserRateLimit> ipRateLimits = new ConcurrentHashMap<>();
    private final Map<String, UserRateLimit> loginRateLimits = new ConcurrentHashMap<>();
    private final Map<String, UserRateLimit> writeRateLimits = new ConcurrentHashMap<>();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String ip = request.getRemoteAddr();
        String username = extractUsername(request);
        String path = request.getServletPath();
        String method = request.getMethod();

        // Skip IP restrictions for auth endpoints
        if (path.startsWith("/auth/")) {
            filterChain.doFilter(request, response);
            return;
        }

        // IP restriction using database configuration
        if (!isIpAllowed(ip)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "IP not allowed");
            return;
        }

        // Connection limit per IP
        ipConnectionCount.putIfAbsent(ip, new AtomicInteger(0));
        int currentIpConn = ipConnectionCount.get(ip).incrementAndGet();
        if (currentIpConn > MAX_CONNECTIONS_PER_IP) {
            ipConnectionCount.get(ip).decrementAndGet();
            response.sendError(429, "Too many connections from this IP");
            return;
        }

        // Session limit per user (if username is available)
        if (username != null) {
            userSessionCount.putIfAbsent(username, new AtomicInteger(0));
            int currentUserSessions = userSessionCount.get(username).incrementAndGet();
            if (currentUserSessions > MAX_SESSIONS_PER_USER) {
                userSessionCount.get(username).decrementAndGet();
                response.sendError(429, "Too many sessions for this user");
                return;
            }
        }

        // General rate limiting by IP (for all requests)
        ipRateLimits.putIfAbsent(ip, new UserRateLimit(60, MAX_REQUESTS_PER_USER_PER_MIN));
        if (!ipRateLimits.get(ip).allow()) {
            response.sendError(429, "Rate limit exceeded (all requests)");
            return;
        }
        
        // Login attempts rate limiting by IP
        if (path.startsWith("/auth/login")) {
            loginRateLimits.putIfAbsent(ip, new UserRateLimit(300, MAX_LOGIN_ATTEMPTS_PER_5MIN));
            if (!loginRateLimits.get(ip).allow()) {
                response.sendError(429, "Too many login attempts");
                return;
            }
        }
        
        // Rate limiting for authenticated users
        if (username != null) {
            // Write actions
            if (method.equals("POST") || method.equals("PUT") || method.equals("DELETE")) {
                writeRateLimits.putIfAbsent(username, new UserRateLimit(60, MAX_WRITE_ACTIONS_PER_MIN));
                if (!writeRateLimits.get(username).allow()) {
                    response.sendError(429, "Too many write actions");
                    return;
                }
            }
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            ipConnectionCount.get(ip).decrementAndGet();
            if (username != null) {
                userSessionCount.get(username).decrementAndGet();
            }
        }
    }

    private boolean isIpAllowed(String ip) {
        List<String> allowedIps = ipConfigService.getAllowedIps();
        for (String allowedIp : allowedIps) {
            if (allowedIp.endsWith("*")) {
                // Wildcard matching
                String prefix = allowedIp.substring(0, allowedIp.length() - 1);
                if (ip.startsWith(prefix)) {
                    return true;
                }
            } else if (ip.equals(allowedIp)) {
                // Exact match
                return true;
            }
        }
        return false;
    }

    private String extractUsername(HttpServletRequest request) {
        // Try to extract username from JWT for authenticated requests
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            // For now, return null to avoid dependency
            return null;
        }
        return null;
    }

    private static class UserRateLimit {
        private final int windowSeconds;
        private final int maxRequests;
        private int requestCount;
        private long windowStart;

        public UserRateLimit(int windowSeconds, int maxRequests) {
            this.windowSeconds = windowSeconds;
            this.maxRequests = maxRequests;
            this.windowStart = Instant.now().getEpochSecond();
            this.requestCount = 0;
        }

        public synchronized boolean allow() {
            long now = Instant.now().getEpochSecond();
            if (now - windowStart >= windowSeconds) {
                windowStart = now;
                requestCount = 1;
                return true;
            } else {
                if (requestCount < maxRequests) {
                    requestCount++;
                    return true;
                } else {
                    return false;
                }
            }
        }
    }
}