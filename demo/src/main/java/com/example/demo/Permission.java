package com.example.demo;

import jakarta.persistence.*;

@Entity
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String permission;

    @Column(nullable = false)
    private Long userId;

    public Permission() {}

    public Permission(String permission, Long userId) {
        this.permission = permission;
        this.userId = userId;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getPermission() { return permission; }
    public void setPermission(String permission) { this.permission = permission; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
} 