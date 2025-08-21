package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {
        System.out.println("Login attempt for username: " + request.getUsername());
        
        Optional<UserModel> userOpt = userRepository.findByUsername(request.getUsername());
        if (userOpt.isEmpty()) {
            System.out.println("Login failed: User not found");
            throw new RuntimeException("Invalid username or password");
        }
        UserModel user = userOpt.get();
        System.out.println("User found: " + user.getUsername());
        
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            System.out.println("Login failed: Password mismatch");
            throw new RuntimeException("Invalid username or password");
        }
        System.out.println("Password verified successfully");
        
        String jwt = jwtUtil.generateToken(user.getUsername());
        System.out.println("JWT token generated: " + jwt.substring(0, 50) + "...");
        
        return new AuthResponse(jwt);
    }

    @PostMapping("/register")
    public String register(@RequestBody AuthRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return "Username already exists";
        }
        String hashedPassword = passwordEncoder.encode(request.getPassword());
        UserModel user = new UserModel(request.getUsername(), hashedPassword);
        userRepository.save(user);
        return "User registered successfully";
    }
} 