package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Autowired
    private KeyService keyService;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        for (Key key : keyService.getValidationKeys()) {
            try {
                JwtParser parser = Jwts.parserBuilder().setSigningKey(key).build();
                Jws<Claims> claimsJws = parser.parseClaimsJws(token);
                return claimsJws.getBody();
            } catch (Exception e) {
                // Try next key
            }
        }
        throw new RuntimeException("Invalid JWT token");
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(keyService.getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        try {
            final String username = extractUsername(token);
            boolean usernameMatch = username.equals(userDetails.getUsername());
            boolean notExpired = !isTokenExpired(token);
            
            System.out.println("JWT Validation - Token username: " + username);
            System.out.println("JWT Validation - UserDetails username: " + userDetails.getUsername());
            System.out.println("JWT Validation - Username match: " + usernameMatch);
            System.out.println("JWT Validation - Token not expired: " + notExpired);
            
            return (usernameMatch && notExpired);
        } catch (Exception e) {
            System.err.println("JWT Validation - Error validating token: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public Boolean validateToken(String token, String username) {
        try {
            final String tokenUsername = extractUsername(token);
            boolean usernameMatch = tokenUsername.equals(username);
            boolean notExpired = !isTokenExpired(token);
            
            System.out.println("JWT Validation - Token username: " + tokenUsername);
            System.out.println("JWT Validation - Expected username: " + username);
            System.out.println("JWT Validation - Username match: " + usernameMatch);
            System.out.println("JWT Validation - Token not expired: " + notExpired);
            
            return (usernameMatch && notExpired);
        } catch (Exception e) {
            System.err.println("JWT Validation - Error validating token: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
} 