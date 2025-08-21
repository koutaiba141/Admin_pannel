package com.example.demo;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.util.Arrays;
import java.util.List;

@Service
public class KeyService {

    private Key newKey;
    private Key oldKey;

    @PostConstruct
    public void init() {
        newKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        oldKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

    @Scheduled(fixedRate = 24 * 60 * 60 * 1000) 
    public void rotateKeys() {
        oldKey = newKey;
        newKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

    public Key getSigningKey() {
        return newKey;
    }

    public List<Key> getValidationKeys() {
        return Arrays.asList(newKey, oldKey);
    }
} 